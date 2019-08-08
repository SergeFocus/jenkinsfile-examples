// https://jenkins.io/doc/book/pipeline/docker

// The following example will cache ~/.m2 between Pipeline runs utilizing the maven container, thereby avoiding the need to re-download dependencies for subsequent runs of the Pipeline.
pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B'
            }
        }
    }
}
