// https://jenkins.io/doc/book/pipeline/docker

// 1. By committing this to the -->root of the source repository<--, the Jenkinsfile can be changed to build a container based on this Dockerfile and then run the defined steps using that container:
// 2. Pipeline provides a global option in the -->Manage Jenkins page<--, and on the Folder level, for specifying which agents (by Label) to use for running Docker-based Pipelines.
pipeline {
    agent { dockerfile true }
    stages {
        stage('Test') {
            steps {
                sh 'node --version'
                sh 'svn --version'
            }
        }
    }
}
