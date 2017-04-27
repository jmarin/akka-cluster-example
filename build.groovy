node('') {

    def owner = "jmarin"
    def project_root_name = "akka-cluster-example"

    stage('Git') {
        git url: 'https://github.com/jmarin/akka-cluster-example.git', branch: 'test'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.1'
        sh "${tool name: 'sbt 0.13.1', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean assembly"
    }

    docker.withRegistry('', 'dtr') {
      stage('Docker Build') {
          sh "cp seed/target/scala-2.12/seed.jar target/scala-2.12/seed.jar"
          sh "cp frontend/target/scala-2.12/frontend.jar target/scala-2.12/frontend.jar"
          sh "cp backend/target/scala-2.12/backend.jar target/scala-2.12/backend.jar"

          def seedName = "seed"
          def frontendName = "frontend"
          def backendName = "backend"

          sh "git rev-parse HEAD > .git/commit-id"
          def commit_id = readFile('.git/commit-id').trim()

          def seedImage = docker.build("${owner}/${project_root_name}-${seedName}:${commit_id}", "${seedName}/.")
          seedImage.push(commit_id)
          seedImage.push('latest')

          def frontendImage = docker.build("${owner}/${project_root_name}-${frontendName}:${commit_id}", "${frontendName}/.")
          frontendImage.push(commit_id)
          frontendImage.push('latest')

          def backendImage = docker.build("${owner}/${project_root_name}-${backendName}:${commit_id}", "${backendName}/.")
          backendImage.push(commit_id)
          backendImage.push('latest')
      }
    }
}
