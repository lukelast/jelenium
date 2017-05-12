pipeline {
  agent {
    docker {
      image 'maven:3.5-jdk-8-alpine'
    }
    
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn install'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts(artifacts: 'target/*jelenium*.jar', fingerprint: true, onlyIfSuccessful: true)
      }
    }
  }
}