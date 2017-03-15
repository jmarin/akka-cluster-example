node {
    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.13'
        sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
    }

    stage('Docker Build') {
        sh "cp seed/target/scala-2.12/seed.jar target/scala-2.12/."
        sh "cp frontend/target/scala-2.12/frontend.jar target/scala-2.12/."
        sh "cp backend/target/scala-2.12/backend.jar target/scala-2.12/."

    }
}