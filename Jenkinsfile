pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh '''
		    chmod +x ./mvnw
		    BOOKSHOP_APP_RESOURCE=/var/jenkins_home/workspace/pipeline-bookshop-spring/src/main/resources
		    cp -r /var/jenkins_home/bookshop_config/spring/application.yml  $BOOKSHOP_APP_RESOURCE/
		    cp -r /var/jenkins_home/bookshop_config/spring/keystore  $BOOKSHOP_APP_RESOURCE
		    docker-compose -f docker-compose-build.yml build
		  '''
            }
        }
    }
}
