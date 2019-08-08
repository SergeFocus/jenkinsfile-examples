#!/usr/bin/env groovy

pipeline {
  agent any

  environment { 
    inputResponse1 = ''
    inputResponse21 = ''
    inputResponse22 = ''
    inputCreds = ''
    inputCredsUsernameAndPassword = ''
    inputFile = ''
    inputTag = ''
    inputLines = ''
    inputPassword = ''
  }

  // This defines job parameters that are populated before job is run or default is used
  parameters {
    booleanParam(defaultValue: true, description: 'Checkbox example', name: 'CheckMe')
    string(defaultValue: '', description: 'String example', name: 'SOME_STRING')
  }

  // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
  options {
    buildDiscarder(logRotator(numToKeepStr:'1'))
    disableConcurrentBuilds()
  }

  stages {
    stage('Input') { // Get input
      steps {
        echo "DEBUG: params.CheckMe: ${params.CheckMe}"
        echo "DEBUG: params.SOME_STRING: ${params.SOME_STRING}"

        script {
            env.inputResponse1 = input([
              message           : 'Please confirm.',
              ok: 'Yes’,

              // submitterParameter: 'user1,user2’
              // submitter: 'user1,user2’

              id: 'ctns-prompt'
              // Allow: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctnsprompt/proceedEmpty
              // Abort: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctns-prompt/abort
            ])

            env.inputResponse21 = input([
              message           : 'Please confirm.',
              // submitterParameter: 'user1,user2’
              parameters        : [
                // Boolean
                [$class: 'BooleanParameterDefinition', defaultValue: true, name: 'param1', description: 'description1'],
              ],
            ])

            env.inputResponse22 = input([
              message           : 'Please confirm.',
              // submitterParameter: 'user1,user2’
              parameters        : [
                // Choice
                [$class: 'ChoiceParameterDefinition', choices: 'choice1\nchoice2', name: 'param2', description: 'description2']
              ],
            ])

            env.inputCreds = input([
              message: '<message>',
              parameters: [
                // Credentials (SSH key)
                [$class: 'CredentialsParameterDefinition',
                 credentialType: 'com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey',
                 defaultValue: 'jenkins2-ssh',
                 description: 'SSH key for access’,
                 name: 'SSH',
                 required: true]
              ]

            env.inputCredsUsernameAndPassword = input([
              message: '<message>',
              parameters: [
                // Credentials (username and password):
                [$class: 'CredentialsParameterDefinition',
                 credentialType: 'com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl',
                 defaultValue: '',
                 description: 'Enter username and password',
                 name: 'User And Pass',
                 required: true]
              ]

            env.InputFile = input([
              message: '<message>',
              parameters: [
                // File
                file(description: 'Choose file to upload', name: 'local’)
              ]

            env.inputTag = input([
              message: '<message>',
              parameters: [
                // List Subversion tags
                [$class: 'ListSubversionTagsParameterDefinition',
                credentialsId: 'jenkins2-ssh',
                defaultValue: '',
                maxTags: '',
                name: 'LocalSVN',
                reverseByDate: false,
                reverseByName: false,
                tagsDir: 'file:///svnrepos/gradle-demo',
                tagsFilter: 'rel_*']
              ]

            env.inputLines = input message: '<message>',
                                   parameters: [
                                     // Multiple lines
                                     text(defaultValue: '''line 1 line 2 line 3''',
                                          description: '',
                                          name: 'Input Lines’)
                                   ]

            // Password
            env.inputPassword = input message: '<message>',
                                      parameters: [
                                        password(defaultValue: '',
                                                 description: 'Enter your password.',
                                                 name: 'passwd')
                                      ]
        }

        echo "Input response: ${env.inputResponse1}"
        echo "Input response: ${env.inputResponse2}"
        echo "Input response: ${env.inputCreds}"
        echo "Input response: ${env.inputCredsUsernameAndPassword}"
        echo "Input response: ${env.inputFile}"
        echo "Input response: ${env.inputTag}"
        echo "Input response: ${env.inputLines}"
        echo "Input response: ${env.inputPassword}"
      }
    }

    stage('Build') { // Build code
      steps {
        sh "echo 'Run a shell command'"
      }
    }
  }

}
