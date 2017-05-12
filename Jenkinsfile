pipeline {
  agent {
    dockerfile {
      filename 'test'
    }
    
  }
  stages {
    stage('build') {
      steps {
        sh 'mvn install'
      }
    }
  }
}