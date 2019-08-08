// https://jenkins.io/doc/book/pipeline/docker

// Using the withRun method, implemented in the Docker Pipeline plugin’s support for Scripted Pipeline, a Jenkinsfile can run MySQL as a sidecar:
node {
    checkout scm
    /*
     * In order to communicate with the MySQL server, this Pipeline explicitly
     * maps the port (`3306`) to a known port on the host machine.
     */
    docker.image('mysql:5').withRun('-e "MYSQL_ROOT_PASSWORD=my-secret-pw" -p 3306:3306') { c -> // sidecar
        /* Wait until mysql service is up */
        sh 'while ! mysqladmin ping -h0.0.0.0 --silent; do sleep 1; done' // agent steps
        /* Run some tests which require MySQL */
        sh 'make check'

        sh "docker logs ${c.id}"
    }
}
