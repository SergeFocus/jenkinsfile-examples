#!/usr/bin/env groovy

/* Only keep the 10 most recent builds. */
def projectProperties = [
    [
        $class: 'BuildDiscarderProperty',
        strategy: [$class: 'LogRotator', numToKeepStr: '10']
    ],
]

// The triggers currently available are cron, pollSCM and upstream.
projectProperties.add(pipelineTriggers([
    pollSCM('H/5 * * * *'),
    githubPush(), // can be used with scripting pipeline only!!!
]))

// projectProperties.add(pipelineTriggers([ githubPush() ]))

// properties([ pipelineTriggers([ pollSCM('*/30 * * * *') ]) ])
// properties([ pipelineTriggers([ githubPush() ]) ])

projectProperties.add(disableConcurrentBuilds())

properties(projectProperties)

try {

  node('worker_node1') {
    stage('Source') { // Get code
      git 'git@github.com:vadym/prometheus.git'
    }

    stage('Build') { // Build code
      sh "echo 'Run a shell command'"
    }
  }

}
catch (exc) {
    echo "Caught: ${exc}"

    String recipient = 'infra@lists.jenkins.example.com'

    mail subject: "${env.JOB_NAME} (${env.BUILD_NUMBER}) failed",
            body: "It appears that ${env.BUILD_URL} is failing, somebody should do something about that",
              to: recipient,
         replyTo: recipient,
            from: 'noreply@ci.jenkins.example.com'

    /* Rethrow to fail the Pipeline properly */
    throw exc
}
