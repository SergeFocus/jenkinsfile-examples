#!/usr/bin/env groovy

pipeline {
  agent any

  // Triggers define how the job is triggered.
  // Jobs may still be triggered manually or by webhook as well here.
  triggers {
    // Scan for SCM changes at 30-minute intervals and start a pipeline execution if any changes has been found
    pollSCM(*/30 * * * *)
  }

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
