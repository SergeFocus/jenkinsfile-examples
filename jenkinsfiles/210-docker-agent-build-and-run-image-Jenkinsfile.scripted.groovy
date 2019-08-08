// https://jenkins.io/doc/book/pipeline/docker

// In order to create a Docker image, the Docker Pipeline plugin also provides a build() method for creating a new image, from a Dockerfile in the repository, during a Pipeline run.
// One major benefit of using the syntax docker.build("my-image-name") is that a Scripted Pipeline can use the return value for subsequent Docker Pipeline calls, for example:
node {
    checkout scm

    def customImage = docker.build("my-image:${env.BUILD_ID}")

    customImage.inside {
        sh 'make test'
    }
}
