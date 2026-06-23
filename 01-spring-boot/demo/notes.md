# Spring Boot Kubernetes Demo

This project demonstrates the deployment of a Spring Boot application on Kubernetes using Minikube. It covers creating a Deployment, exposing it with a Service, performing rolling updates, and observing Kubernetes self-healing.

---

## Docker Images

### Version 1

```text
shaad98/k8s-demo:v1
```

* Basic Spring Boot application.
* Displays application information and Pod hostname.

---

### Version 2

```text
shaad98/k8s-demo:v2
```

* Added Spring Boot Actuator.
* Enabled `/actuator/shutdown` endpoint.
* Shutdown endpoint accepts **POST** requests only.

Example:

```bash
curl -X POST http://<host>:<port>/actuator/shutdown
```

---

### Version 3

```text
shaad98/k8s-demo:v3
```

* Added a custom **GET** endpoint to trigger application shutdown.
* Useful for demonstrating Kubernetes self-healing directly from the browser.

Example:

```text
http://<host>:<port>/shutdown
```

---

# Start Minikube

```bash
minikube start
```

Check cluster status:

```bash
kubectl cluster-info
```

---

# Create Deployment

```bash
kubectl create deployment backend --image=shaad98/k8s-demo:v1 --replicas=2
```

Verify:

```bash
kubectl get deployments
kubectl get pods
```

---

# Expose Deployment as a Service

```bash
kubectl expose deployment backend \
    --type=NodePort \
    --port=8080 \
    --target-port=8080
```

Verify:

```bash
kubectl get svc
```

Open the application:

```bash
minikube service backend
```

or

```bash
minikube service backend --url
```

---

# Describe Deployment

```bash
kubectl describe deployment backend
```

---

# Describe Pod

```bash
kubectl describe pod <pod-name>
```

This command is useful for:

* Viewing container information.
* Finding the container name.
* Checking events.
* Diagnosing image pull failures.
* Viewing environment variables.
* Checking mounted volumes.

---

# Get Container Name

```bash
kubectl describe deployment backend
```

or

```bash
kubectl get deployment backend -o yaml
```

Example:

```yaml
containers:
- name: backend
  image: shaad98/k8s-demo:v1
```

The container name (`backend`) is required when updating the Deployment image.

---

# Update Deployment Image

Example:

```bash
kubectl set image deployment/backend backend=shaad98/k8s-demo:v2
```

Update to Version 3:

```bash
kubectl set image deployment/backend backend=shaad98/k8s-demo:v3
```

Watch the rolling update:

```bash
kubectl rollout status deployment/backend
```

View Pods:

```bash
kubectl get pods -w
```

---

# View Logs

```bash
kubectl logs <pod-name>
```

Follow logs continuously:

```bash
kubectl logs -f <pod-name>
```

---

# Scale Deployment

Increase replicas:

```bash
kubectl scale deployment backend --replicas=4
```

Decrease replicas:

```bash
kubectl scale deployment backend --replicas=2
```

---

# Verify Service Endpoints

```bash
kubectl get endpoints backend
```

---

# Delete Service

```bash
kubectl delete service backend
```

---

# Delete Deployment

```bash
kubectl delete deployment backend
```

---

# Useful Commands

List Deployments:

```bash
kubectl get deployments
```

List Pods:

```bash
kubectl get pods
```

Watch Pods:

```bash
kubectl get pods -w
```

List Services:

```bash
kubectl get svc
```

Describe Service:

```bash
kubectl describe svc backend
```

Describe Pod:

```bash
kubectl describe pod <pod-name>
```

Check Rollout History:

```bash
kubectl rollout history deployment/backend
```

Restart Deployment:

```bash
kubectl rollout restart deployment/backend
```

Rollback Deployment:

```bash
kubectl rollout undo deployment/backend
```

---

# Learning Objectives

* Build a Docker image for a Spring Boot application.
* Deploy applications on Kubernetes using a Deployment.
* Expose applications using a NodePort Service.
* Perform rolling updates with zero downtime.
* Observe Kubernetes self-healing by terminating application instances.
* Understand Pod lifecycle and ReplicaSets.
* Display the serving Pod's hostname to observe request routing.
* Practice scaling applications using multiple replicas.
