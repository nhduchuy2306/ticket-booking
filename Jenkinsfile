pipeline {
  agent any

  triggers {
      githubPush()
  }

  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }

  parameters {
    booleanParam(name: 'DEPLOY_STACK', defaultValue: false, description: 'Deploy infrastructure/docker stack after successful build')
    string(name: 'ENV_FILE_PATH', defaultValue: '.env', description: 'Path (relative to repo root) to env file used by docker compose deploy')
  }

  environment {
    MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build And Test') {
      steps {
        sh 'mvn -B -ntp clean test -P prod'
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B -ntp package -DskipTests -P prod'
      }
      post {
        success {
          archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
      }
    }

    stage('Deploy Docker Stack') {
      when {
        expression { return params.DEPLOY_STACK }
      }
      steps {
        sh '''
          set -eu
          if [ ! -f "${ENV_FILE_PATH}" ]; then
            echo "Missing env file: ${ENV_FILE_PATH}"
            exit 1
          fi

          docker compose \
            -f infrastructure/dockers/docker-compose.yml \
            --env-file "${ENV_FILE_PATH}" \
            up -d --build
        '''
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}

