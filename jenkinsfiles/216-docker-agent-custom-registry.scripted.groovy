// https://jenkins.io/doc/book/pipeline/docker

// By default the Docker Pipeline integrates assumes the default Docker Registry of Docker Hub.
// In order to use a custom Docker Registry, users of Scripted Pipeline can wrap steps with the withRegistry() method, passing in the custom Registry URL, for example:
node {
    checkout scm

    docker.withRegistry('https://registry.example.com') {

        docker.image('my-custom-image').inside {
            sh 'make test'
        }
    }
}
