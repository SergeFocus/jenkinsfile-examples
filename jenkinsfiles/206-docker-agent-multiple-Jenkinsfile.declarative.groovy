// https://jenkins.io/doc/book/pipeline/docker

// Combining Docker and Pipeline allows a Jenkinsfile to use multiple types of technologies by combining the agent {} directive, with different stages.
pipeline {
    agent none
    stages {
        stage('Back-end') {
            agent {
                docker { image 'maven:3-alpine' }
            }
            steps {
                sh 'mvn --version'
            }
        }
        stage('Front-end') {
            agent {
                docker { image 'node:7-alpine' }
            }
            steps {
                sh 'node --version'
            }
        }
    }
}
