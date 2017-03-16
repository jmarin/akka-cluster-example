node('') {

    def project_name = "akka-cluster-example"

    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.1'
        sh "${tool name: 'sbt 0.13.1', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
    }

    docker.withRegistry('', 'dtr') {
      stage('Seed Docker Build') {
          sh "git rev-parse HEAD > .git/commit-id"
          def commit_id = readFile('.git/commit-id').trim()
          println(commit_id)

          def seedImage = docker.build("jmarin/${project_name}-seed:${commit_id}", "./seed")
          seedImage.push(commit_id)
          seedImage.push('latest')
      }
    }
}