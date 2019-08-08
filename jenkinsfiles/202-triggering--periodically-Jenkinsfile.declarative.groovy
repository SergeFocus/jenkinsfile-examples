#!/usr/bin/env groovy

pipeline {
  agent any

  // Triggers define how the job is triggered.
  triggers {
    // Start a pipeline execution once a day
    cron('@daily')

    // Start a pipeline execution once per half an hour
    //cron('H/30 * * * *')

    // Start a pipeline execution at 15 minutes past the hour
    //cron(0 9 * * 1-5)

    // Start a pipeline execution at 15 minutes past the hour
    //cron(15 * * * *)

    // Start a pipeline session at some point in a range 0-30 minutes after the hour
    //cron(H(0,30) * * * *)

    // Start a pipeline execution at 9 a.m. Monday through Friday
    //cron(0 9 * * 1-5)
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
