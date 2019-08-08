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
    // Start a pipeline execution once a day
    cron('@daily'),

    // Start a pipeline execution once per half an hour
    //cron('H/30 * * * *'),

    // Start a pipeline execution at 15 minutes past the hour
    //cron(0 9 * * 1-5),

    // Start a pipeline execution at 15 minutes past the hour
    //cron(15 * * * *),

    // Start a pipeline session at some point in a range 0-30 minutes after the hour
    //cron(H(0,30) * * * *),

    // Start a pipeline execution at 9 a.m. Monday through Friday
    //cron(0 9 * * 1-5),
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
