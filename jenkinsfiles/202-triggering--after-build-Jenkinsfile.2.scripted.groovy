#!/usr/bin/env groovy

try {
  node('worker_node1') {
    stage("Calling Downstream Job") {
      git 'git@github.com:vadym/prometheus.git'
    }

    stage("Calling Downstream Job") {
      job_downstream = build(job: "DS_JOB1",
         parameters: [[$class: 'StringParameterValue', name: 'PLATFORM', value: "pf-1"],
                      [$class: 'StringParameterValue', name: 'PROJECT', value: "Dummy1"]],
         propagate: false,
         // quietPeriod: 60,
         wait: true)
      if(job_downstream?.result.toString() == 'FAILURE') {
        currentBuild.result = job_downstream?.result.toString()
        println("Downstream job for PLATFORM: ${PLATFORM}")
      }
    }

    stage('Build') { // Build code
      sh "echo 'Run a shell command'"
    }
  }
}
catch (exc) {
    echo "Caught: ${exc}"
    println(err)
    currentBuild.result = 'FAILURE'
}
finally {
    // step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: '<GroupMail_ID>', sendToIndividuals: false])

    String recipient = 'infra@lists.jenkins.example.com'

    mail subject: "${env.JOB_NAME} (${env.BUILD_NUMBER}) failed",
            body: "It appears that ${env.BUILD_URL} is failing, somebody should do something about that",
              to: recipient,
         replyTo: recipient,
            from: 'noreply@ci.jenkins.example.com'

    /* Rethrow to fail the Pipeline properly */
    throw exc
}
