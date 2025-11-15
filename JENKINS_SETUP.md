# Jenkins Setup Guide

## Quick Setup

### 1. Update Git URL in Jenkinsfile
Replace the Git repository URL in the Jenkinsfile:
```groovy
git branch: 'main', url: 'https://github.com/YOUR_USERNAME/demo.git'
```

### 2. No Additional Credentials Needed!
The Jenkinsfile uses local kubeconfig:
```groovy
withEnv(["KUBECONFIG=$HOME/.kube/config"])
```

This automatically uses Jenkins server's kubectl configuration.

---

## Environment Variables (Auto-Set)

```groovy
APP_NAME = "patient-api"
APP_NAMESPACE = "patient-api-ns"
IMAGE_NAME = "patient-api-image"
IMAGE_TAG = "${BUILD_NUMBER}"
APP_PORT = 8080
NODE_PORT = 30080
REPLICA_COUNT = 2
```

---

## Pipeline Stages

1. **Checkout** - Clone code from GitHub
2. **Build Maven Project** - Run `mvn clean package`
3. **Run Tests** - Execute unit tests
4. **Cleanup Old Container** - Remove previous Docker containers
5. **Build Docker Image** - Create Docker image with build number
6. **K8s Deployment** - Apply all Kubernetes manifests
7. **Verify Deployment** - Check pods, services, and rollout status

---

## Port Information

| Component | Port | Type | Usage |
|-----------|------|------|-------|
| **Service Port** | **30080** | NodePort | External access |
| **Container Port** | 8080 | HTTP | Internal |
| **H2 Console** | 8080/h2-console | HTTP | Internal only |

**Access API at:** `http://<NODE-IP>:30080/api/patients`

---

## Prerequisites on Jenkins Server

Ensure Jenkins server has:
1. **Docker** installed and running
2. **kubectl** configured with cluster access
3. **Maven** available (or use `./mvnw` wrapper)
4. **kubeconfig** at `~/.kube/config`

Check with:
```bash
# As Jenkins user
docker --version
kubectl cluster-info
mvn --version
```

---

## Create Jenkins Pipeline Job

1. **New Item** → **Pipeline**
2. **Name:** `patient-api-pipeline`
3. **Pipeline section:**
   - **Definition:** Pipeline script from SCM
   - **SCM:** Git
   - **Repository URL:** `https://github.com/YOUR_USERNAME/demo.git`
   - **Branch:** `*/main`
   - **Script Path:** `Jenkinsfile`
4. **Save & Build**

---

## Testing the API After Deployment

### Get Node IP and Port
```bash
# Get node IP
kubectl get nodes -o wide

# Get service details
kubectl get svc patient-api-service -n patient-api-ns
```

### Test Endpoints
```bash
NODE_IP=$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="ExternalIP")].address}')
# If ExternalIP is not available, use InternalIP
NODE_IP=$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}')

# Get all patients
curl http://$NODE_IP:30080/api/patients

# Create patient
curl -X POST http://$NODE_IP:30080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "555-1234",
    "status": "Active"
  }'

# Get patient by ID
curl http://$NODE_IP:30080/api/patients/1

# Update patient
curl -X PUT http://$NODE_IP:30080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName": "Jane"}'

# Delete patient
curl -X DELETE http://$NODE_IP:30080/api/patients/1
```

---

## Kubernetes Monitoring Commands

```bash
# Watch pods
kubectl get pods -n patient-api-ns -w

# View logs
kubectl logs -f deployment/patient-api -n patient-api-ns

# Describe deployment
kubectl describe deployment patient-api -n patient-api-ns

# Describe service
kubectl describe svc patient-api-service -n patient-api-ns

# Check rollout status
kubectl rollout status deployment/patient-api -n patient-api-ns

# View all resources in namespace
kubectl get all -n patient-api-ns

# Access pod shell
kubectl exec -it <pod-name> -n patient-api-ns -- /bin/sh
```

---

## Troubleshooting

### Pods not starting?
```bash
kubectl describe pod <pod-name> -n patient-api-ns
kubectl logs <pod-name> -n patient-api-ns
```

### Image not found?
Ensure image is built locally or available in registry:
```bash
docker images | grep patient-api
```

### Service not accessible?
```bash
# Check service endpoints
kubectl get endpoints patient-api-service -n patient-api-ns

# Check node port
kubectl get svc patient-api-service -n patient-api-ns
```

---

## Rollback on Failure

If deployment fails, Jenkins automatically runs:
```bash
kubectl rollout undo deployment/patient-api -n patient-api-ns
```

Manual rollback:
```bash
kubectl rollout history deployment/patient-api -n patient-api-ns
kubectl rollout undo deployment/patient-api -n patient-api-ns --to-revision=<number>
```

---

## Jenkins Console Output Example

```
✔ Completed: Checkout → Build → Test → Docker Build → K8s Deploy

========== Deployment Successful ==========
Application: patient-api
Namespace: patient-api-ns
Image: patient-api-image:5
Replicas: 2
Port: 30080
Get service: kubectl get svc -n patient-api-ns
```
