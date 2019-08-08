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

try {
    node() {
        stage('Input') {

            milestone 1
            def inputResponse1 = input([
              message           : 'Please confirm.',
              ok: 'Yes’,

              // submitterParameter: 'user1,user2’
              // submitter: 'user1,user2’

              id: 'ctns-prompt'
              // Allow: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctnsprompt/proceedEmpty
              // Abort: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctns-prompt/abort
            ])

            def inputResponse2 = input([
              message           : 'Please confirm.',
              // submitterParameter: 'user1,user2’
              parameters        : [
                // Boolean
                [$class: 'BooleanParameterDefinition', defaultValue: true, name: 'param1', description: 'description1'],

                // Choice
                [$class: 'ChoiceParameterDefinition', choices: 'choice1\nchoice2', name: 'param2', description: 'description2']

              ],
              ok: 'Yes’,
              id: 'ctns-prompt'
              // Allow: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctnsprompt/proceedEmpty
              // Abort: http://[jenkins-url]/job/[job_name]/[build_id]/input/Ctns-prompt/abort
            ])

            def inputCreds = input([
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

            def inputCredsUsernameAndPassword = input([
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

            def InputFile = input([
              message: '<message>',
              parameters: [
                // File
                file(description: 'Choose file to upload', name: 'local’)
              ]

            def inputTag = input([
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

            def inputLines = input message: '<message>',
                                   parameters: [
                                     // Multiple lines
                                     text(defaultValue: '''line 1 line 2 line 3''',
                                          description: '',
                                          name: 'Input Lines’)
                                   ]

            // Password
            def inputPassword = input message: '<message>',
                                      parameters: [
                                        password(defaultValue: '',
                                                 description: 'Enter your password.',
                                                 name: 'passwd')
                                      ]

            // Return Values from Multiple Input Parameters (a map is returned)
            def inputLogin = input message: 'Login',
                                  parameters:[
                                    string(defaultValue: '',
                                           description: 'Enter Userid:',
                                           name: 'userid’),
                                    password(defaultValue: '',
                                             description: 'Enter Password:',
                                             name: 'passwd’)
                                  ]

            milestone 2
            echo "Input response: ${inputResponse1}"
            echo "Input response: ${inputResponse2}"
            echo "Input response: ${inputCreds}"
            echo "Input response: ${inputCredsUsernameAndPassword}"
            echo "Input response: ${inputFile}"
            echo "Input response: ${inputTag}"
            echo "Input response: ${inputLines}"
            echo "Input response: ${inputPassword}"

            echo "Username = " + inputLogin['userid’]
            echo "Password = ${inputLogin['passwd']}"
            echo inputLogin.userid + " " + inputLogin.passwd
        }
    }

}
catch (exc) {
    echo "Caught: ${exc}"

    String recipient = 'infra@lists.jenkins-ci.org'

    mail subject: "${env.JOB_NAME} (${env.BUILD_NUMBER}) failed",
            body: "It appears that ${env.BUILD_URL} is failing, somebody should do something about that",
              to: recipient,
         replyTo: recipient,
            from: 'noreply@ci.jenkins.io'

    /* Rethrow to fail the Pipeline properly */
    throw exc
}
