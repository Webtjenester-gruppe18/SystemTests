pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''
        cd ./dockertest;
        docker-compose up -d ;
        cd ..;
        mvn test;
        docker container stop $(docker container ls --filter "env=test");
        docker container rm $(docker container ls --filter "env=test");
        '''
      }
    }
     stage('Deploy') {
          steps {
            sh '''
            docker container stop $(docker container ls --filter "env=prod");
            docker container rm $(docker container ls --filter "env=prod");
            docker-compose up -d ;
            '''
          }
     }
  }
}