// https://jenkins.io/doc/book/pipeline/docker

// Practically any tool which can be packaged in a Docker container. can be used with ease by making only minor edits to a Jenkinsfile.
pipeline {
    agent {
        docker { image 'node:7-alpine' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'node --version'
            }
        }
    }
}
