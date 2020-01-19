pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''
        docker container stop $(docker container ls -aq);
        docker container rm $(docker container ls -aq);
        docker-compose -d up;
        mvn test;
        docker container stop $(docker container ls -aq);
        docker container rm $(docker container ls -aq);
        '''
      }
    }
     stage('Deploy') {
          steps {
            sh '''
            docker-compose -d up;
            '''
          }
     }
  }
}