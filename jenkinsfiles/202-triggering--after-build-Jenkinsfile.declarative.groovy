#!/usr/bin/env groovy

pipeline {
  agent any

  // Triggers define how the job is triggered.
  triggers {
    upstream(upstreamProjects: 'Job1', // (or 'Job1/master','Job2/master')
             threshold: hudson.model.Result.SUCCESS)
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
