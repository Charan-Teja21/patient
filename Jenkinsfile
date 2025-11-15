pipeline {
    agent any

    environment {
        APP_NAME = "patient-api"
        APP_NAMESPACE = "${APP_NAME}-ns"
        IMAGE_NAME = "${APP_NAME}-image"
        IMAGE_TAG = "${BUILD_NUMBER}"
        APP_PORT = 8080
        NODE_PORT = 30080
        REPLICA_COUNT = 2
    }

    stages {

        stage('Checkout') {
            steps {
                echo '========== Checking out code =========='
                git branch: 'main', url: 'https://github.com/Charan-Teja21/patient.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                echo '========== Building Maven project =========='
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                echo '========== Running unit tests =========='
                sh './mvnw test'
            }
        }

        stage('Cleanup Old Container (if exists)') {
            steps {
                echo '========== Cleaning up old containers =========='
                sh '''
                    if [ "$(docker ps -aq -f name=${APP_NAME})" ]; then
                        echo "Container exists — stopping & removing..."
                        docker stop ${APP_NAME} || true
                        docker rm ${APP_NAME} || true
                    else
                        echo "No old container found."
                    fi
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '========== Building Docker image =========='
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -f Dockerfile ."
                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('K8s Deployment') {
            steps {
                echo '========== Deploying to Kubernetes =========='
                script {
                    withEnv(["KUBECONFIG=$HOME/.kube/config"]) {
                        sh '''
                            echo "Creating namespace..."
                            kubectl create namespace ${APP_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -
                            
                            echo "Applying ConfigMap..."
                            kubectl apply -f k8s/configmap.yaml
                            
                            echo "Applying Deployment..."
                            kubectl apply -f k8s/deployment.yaml
                            
                            echo "Applying Service..."
                            kubectl apply -f k8s/service.yaml
                            
                            echo "Waiting for deployment to be ready..."
                            kubectl rollout status deployment/patient-api -n ${APP_NAMESPACE} --timeout=5m
                        '''
                    }
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                echo '========== Verifying deployment =========='
                script {
                    withEnv(["KUBECONFIG=$HOME/.kube/config"]) {
                        sh '''
                            echo "========== Namespace Status =========="
                            kubectl get ns ${APP_NAMESPACE}
                            
                            echo ""
                            echo "========== Service Details =========="
                            kubectl get svc patient-api-service -n ${APP_NAMESPACE}
                            
                            echo ""
                            echo "========== Pod Status =========="
                            kubectl get pods -n ${APP_NAMESPACE}
                            
                            echo ""
                            echo "========== Deployment Status =========="
                            kubectl get deployment patient-api -n ${APP_NAMESPACE}
                            
                            echo ""
                            echo "========== Service Endpoint =========="
                            ENDPOINT=$(kubectl get svc patient-api-service -n ${APP_NAMESPACE} -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
                            if [ -z "$ENDPOINT" ]; then
                                ENDPOINT=$(kubectl get svc patient-api-service -n ${APP_NAMESPACE} -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')
                            fi
                            echo "Service accessible at: http://$ENDPOINT:${NODE_PORT}/api/patients"
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✔ Completed: Checkout → Build → Test → Docker Build → K8s Deploy"
            echo "========== Deployment Successful =========="
            echo "Application: ${APP_NAME}"
            echo "Namespace: ${APP_NAMESPACE}"
            echo "Image: ${IMAGE_NAME}:${IMAGE_TAG}"
            echo "Replicas: ${REPLICA_COUNT}"
            echo "Port: ${NODE_PORT}"
            echo "Get service: kubectl get svc -n ${APP_NAMESPACE}"
        }
        failure {
            echo "❌ Pipeline Failed"
            script {
                withEnv(["KUBECONFIG=$HOME/.kube/config"]) {
                    sh '''
                        echo "Rolling back deployment..."
                        kubectl rollout undo deployment/patient-api -n ${APP_NAMESPACE} || true
                    '''
                }
            }
        }
    }
}
