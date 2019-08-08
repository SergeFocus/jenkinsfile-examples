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
    upstream(upstreamProjects: 'Job1', // (or 'Job1/master','Job2/master')
             threshold: hudson.model.Result.SUCCESS),
]))

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
