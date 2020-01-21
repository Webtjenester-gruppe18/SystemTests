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
        docker container stop $(docker container ls --filter "label=test");
        docker container rm $(docker container ls --filter "label=test");
        '''
      }
    }
     stage('Deploy') {
          steps {
            sh '''
            docker container stop $(docker container ls --filter "label=prod");
            docker container rm $(docker container ls --filter "label=prod");
            docker-compose up -d ;
            '''
          }
     }
  }
}