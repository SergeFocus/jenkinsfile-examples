// https://jenkins.io/doc/book/pipeline/docker

// One common usage of image "tags" is to specify a latest tag for the most recently, validated, version of a Docker image. The push() method accepts an optional tag parameter, allowing the Pipeline to push the customImage with different tags, for example:
node {
    checkout scm
    def customImage = docker.build("my-image:${env.BUILD_ID}")
    customImage.push()

    customImage.push('latest')
}
