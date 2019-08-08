// https://jenkins.io/doc/book/pipeline/docker

// It is possible to pass other arguments to docker build by adding them to the second argument of the build() method. When passing arguments this way, the last value in the that string must be the path to the docker file and should end with the folder to use as the build context)
// This example overrides the default Dockerfile by passing the -f flag:
// Builds my-image:${env.BUILD_ID} from the Dockerfile found at ./dockerfiles/Dockerfile.test.
node {
    checkout scm
    def dockerfile = 'Dockerfile.test'
    def customImage = docker.build("my-image:${env.BUILD_ID}", "-f ${dockerfile} ./dockerfiles") 
}
