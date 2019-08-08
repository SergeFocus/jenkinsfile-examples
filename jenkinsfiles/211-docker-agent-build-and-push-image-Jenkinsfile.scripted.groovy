// https://jenkins.io/doc/book/pipeline/docker

// The return value can also be used to publish the Docker image to Docker Hub, or a custom Registry, via the push() method, for example:
node {
    checkout scm
    def customImage = docker.build("my-image:${env.BUILD_ID}")
    customImage.push()
}
