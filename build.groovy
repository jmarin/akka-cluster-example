node('') {
    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.1'
        sh "${tool name: 'sbt 0.13.1', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
    }

    docker.withRegistry('', 'dtr') {
      stage('Docker Build') {
          sh "git rev-parse HEAD > .git/commit-id"
          def commit_id = readFile('.git/commit-id').trim()
          println(commit_id)

          def seedImage = docker.build("jmarin/akka-cluster-example-seed", "./seed")
          seedImage.push()
      }
    }
}