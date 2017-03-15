node {
    def branchVersion = ""

    tools {
        sbt 'sbt-0.13.13'
        jdk 'jdk8'
    }

    stage('Checkout') {
       // checkout repository\
        checkout scm

        sh "git checkout ${env.BRANCH_NAME}"
    }



//   stage('Build') {
//       def builds = [:]
//       builds['seed'] = {
//           // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.13'
//           sh "${tool name: 'sbt-0.13.13', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
//       }
//   }


}