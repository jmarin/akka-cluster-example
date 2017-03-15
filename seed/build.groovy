node {
    def branchVersion = ""

    stage('Checkout') {
       // checkout repository\
        checkout scm

        sh "git checkout ${caller.env.BRANCH_NAME}"
    }

    stage('Determine Branch Version') {
        // add sbt to path
        env.PATH = "${tool 'sbt'}/bin:${env.PATH}"

        branchVersion = env.BRANCH_NAME
    }

    stage('Package project') {
        sh 'sbt clean test assembly'
    }
}