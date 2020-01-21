pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''
        docker-compose up -d ;
        mvn test;
        docker system prune;
        '''
      }
    }

  }
}