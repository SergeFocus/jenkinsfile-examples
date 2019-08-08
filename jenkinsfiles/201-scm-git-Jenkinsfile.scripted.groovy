#!/usr/bin/env groovy

// Scripted Pipeline
node('worker_node1') {
  stage('Source') { // Get code
    git 'git@github.com:vadym/prometheus.git'
  }

  stage('Build') { // Build code
    sh "echo 'Run a shell command'"
  }
}
