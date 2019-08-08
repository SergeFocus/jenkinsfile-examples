// https://jenkins.io/doc/book/pipeline/docker

// The build() method builds the Dockerfile in the current directory by default. This can be overridden by providing a directory path containing a Dockerfile as the second argument of the build() method, for example:
// Builds 'test-image' from the Dockerfile found at ./dockerfiles/test/Dockerfile.
node {
    checkout scm
    def testImage = docker.build("test-image", "./dockerfiles/test") 

    testImage.inside {
        sh 'make test'
    }
}
