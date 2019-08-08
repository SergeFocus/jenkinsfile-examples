#!/usr/bin/env groovy

pipeline {
  agent any

  // This allows for triggering builds by accessing a specific URL for the given job on the Jenkins system (via a hook or a script).
  // Note: an authorization token is required (set it on the job config page)

  // Call the URL (and optionally append “&cause=Cause+Text” to provide text that will be included in the recorded build cause):
  // JENKINS_URL/job/test1/build?token=TOKEN_NAME or
  //            /buildWithParameters?token=TOKEN_NAME

  // Sending "String Parameters": curl -X POST JENKINS_URL/job/JOB_NAME/build \
  // --user USER:TOKEN \
  // --data-urlencode json='{"parameter": [{"name":"id", "value":"123"}, {"name":"verbosity", "value":"high"}]}’

  // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
  options {
    buildDiscarder(logRotator(numToKeepStr:'1'))
    disableConcurrentBuilds()
  }

  stages {
    stage('Source') { // Get code
      steps {
        git 'git@github.com:vadym/prometheus.git'
      }
    }

    stage('Build') { // Build code
      steps {
        sh "echo 'Run a shell command'"
      }
    }
  }

}
