#!/usr/bin/env groovy

/* Only keep the 10 most recent builds. */
def projectProperties = [
    [
        $class: 'BuildDiscarderProperty',
        strategy: [$class: 'LogRotator', numToKeepStr: '10']
    ],
]

projectProperties.add(disableConcurrentBuilds())

properties(projectProperties)

// This allows for triggering builds by accessing a specific URL for the given job on the Jenkins system (via a hook or a script).
// Note: an authorization token is required (set it on the job config page)

// Call the URL (and optionally append “&cause=Cause+Text” to provide text that will be included in the recorded build cause):
// JENKINS_URL/job/test1/build?token=TOKEN_NAME or
//            /buildWithParameters?token=TOKEN_NAME

// Sending "String Parameters": curl -X POST JENKINS_URL/job/JOB_NAME/build \
// --user USER:TOKEN \
// --data-urlencode json='{"parameter": [{"name":"id", "value":"123"}, {"name":"verbosity", "value":"high"}]}’

try {

  node() {
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
