#!/usr/bin/env groovy

pipeline { // Declarative Pipeline
  agent any

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
