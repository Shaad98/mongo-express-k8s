# Kubernetes Nginx Deployment and Service

## Overview

This project demonstrates how to deploy an Nginx web server on Kubernetes using two fundamental Kubernetes resources:

* **Deployment** - Manages Pods and ensures the desired number of replicas are always running.
* **Service** - Provides a stable network endpoint to access the Pods created by the Deployment.

---

## Project Structure

```text
.
├── nginx.yaml
└── README.md
```

The `nginx.yaml` file contains both the Deployment and Service definitions separated by `---`.

---

# Deployment

The Deployment is responsible for creating and managing Pods.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx
```

### Configuration

| Field         | Description                                                         |
| ------------- | ------------------------------------------------------------------- |
| apiVersion    | Specifies the Kubernetes API version used for Deployment resources. |
| kind          | Defines the type of Kubernetes resource.                            |
| metadata.name | Name of the Deployment object.                                      |

---

### Replicas

```yaml
replicas: 1
```

This instructs Kubernetes to maintain one running Pod.

If the Pod crashes or is deleted, Kubernetes automatically creates another Pod to maintain the desired state.

---

### Selector

```yaml
selector:
  matchLabels:
    app: nginx
```

The selector tells the Deployment which Pods belong to it.

Only Pods with the label

```text
app=nginx
```

are managed by this Deployment.

---

### Pod Template

```yaml
template:
  metadata:
    labels:
      app: nginx
```

The template acts as a blueprint for creating Pods.

Every Pod created by the Deployment receives the label:

```text
app=nginx
```

These labels must match the Deployment selector.

---

### Container Specification

```yaml
containers:
  - name: nginx
    image: nginx:1.27
```

This defines the container running inside the Pod.

* Container Name: nginx
* Docker Image: nginx version 1.27

---

### Container Port

```yaml
ports:
  - containerPort: 80
```

This specifies that the Nginx application listens on port 80 inside the container.

---

# Service

A Service provides a stable network endpoint for accessing Pods.

Unlike Pods, Services keep the same IP address even if Pods are recreated.

```yaml
apiVersion: v1
kind: Service
```

---

### Selector

```yaml
selector:
  app: nginx
```

The Service routes traffic only to Pods having the label

```text
app=nginx
```

This label matches the Pods created by the Deployment.

---

### Ports

```yaml
ports:
  - protocol: TCP
    port: 80
    targetPort: 80
```

| Field      | Description                                       |
| ---------- | ------------------------------------------------- |
| protocol   | Network protocol used by the Service.             |
| port       | Port exposed by the Service.                      |
| targetPort | Port on the container where traffic is forwarded. |

Traffic flow:

```text
Client
   │
   ▼
Service :80
   │
   ▼
Pod :80
   │
   ▼
Nginx Container
```

---

### Service Type

```yaml
type: ClusterIP
```

ClusterIP is the default Service type.

The application is accessible only from within the Kubernetes cluster.

---

# Resource Relationship

```text
Deployment
      │
      ▼
ReplicaSet
      │
      ▼
Pods (app=nginx)
      ▲
      │
Service Selector
      │
      ▼
ClusterIP Service
```

---

# Deploy the Application

Create both resources:

```bash
kubectl apply -f nginx.yaml
```

Verify the Deployment:

```bash
kubectl get deployments
```

Verify the Pods:

```bash
kubectl get pods
```

Verify the Service:

```bash
kubectl get svc
```

View the Service endpoints:

```bash
kubectl get endpoints
```

---

# Expected Result

* One Nginx Pod is running.
* A ClusterIP Service is created.
* The Service automatically routes traffic to the Nginx Pod using label selectors.
* If the Pod fails, Kubernetes creates a replacement automatically while the Service continues routing traffic to the new Pod.

---

# Key Concepts Learned

* Kubernetes Deployment
* Kubernetes Service
* Labels
* Selectors
* Pod Template
* Replica Management
* Container Images
* Container Ports
* ClusterIP Networking
* Desired State Management
