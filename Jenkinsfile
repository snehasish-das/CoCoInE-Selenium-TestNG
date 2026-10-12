pipeline {
  agent { label "${params.AGENT_LABEL}" }

  parameters {
    choice(name: 'ENV', choices: ['local', 'dev', 'qa', 'stage', 'prod-like'], description: 'Target environment')
    choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser for UI execution')
    choice(name: 'SUITE', choices: ['ci', 'sanity', 'prod', 'local'], description: 'Maven profile/suite to execute')
    string(name: 'THREAD_COUNT', defaultValue: '4', description: 'Parallel thread count')
    string(name: 'AGENT_LABEL', defaultValue: 'aws-linux', description: 'Jenkins/AWS agent label')
    booleanParam(name: 'START_GRID', defaultValue: true, description: 'Start Docker Selenium Grid')
  }

  environment {
    MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Grid') {
      when { expression { return params.START_GRID } }
      steps {
        sh 'docker compose -f grid/docker-compose.yml up -d'
      }
    }

    stage('Quality Gates') {
      steps {
        sh 'mvn -B -DskipTests checkstyle:check spotbugs:check'
      }
    }

    stage('Tests') {
      steps {
        sh """
          mvn -B test -P${params.SUITE} \
            -Denv=${params.ENV} \
            -Dbrowser=${params.BROWSER} \
            -DthreadCount=${params.THREAD_COUNT} \
            -Dremote=${params.START_GRID} \
            -DgridUrl=http://localhost:4444/wd/hub
        """
      }
      post {
        always {
          archiveArtifacts artifacts: 'target/**', allowEmptyArchive: true
          allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
      }
    }
  }

  post {
    always {
      sh 'docker compose -f grid/docker-compose.yml down || true'
    }
  }
}
