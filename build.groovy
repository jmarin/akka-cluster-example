node {
    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Build') {
        def builds = [:]
        builds['seed'] = {
            sh 'cd seed'
            // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.13'
            sh "${tool name: 'sbt-0.13.13', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt compile test"
        }
    }
}