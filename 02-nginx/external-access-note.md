# Exposing an Nginx Application Outside the Kubernetes Cluster

## Overview

Initially, the Nginx application was deployed using a **ClusterIP Service**.

Although the Pod was running successfully, the application could not be accessed from outside the Kubernetes cluster because `ClusterIP` is designed for **internal communication only**.

To make the application accessible externally, the Service configuration was updated.

---

# Initial Configuration

The original Service was configured as:

```yaml
spec:
  type: ClusterIP
```

The Deployment was successfully creating the Nginx Pod.

```text
Deployment
      │
      ▼
ReplicaSet
      │
      ▼
Pod (Running)
```

The Service was also created successfully.

```text
Service
│
└── ClusterIP
```

However, opening the application in a browser was not possible.

---

# Why ClusterIP Didn't Work for External Access

A **ClusterIP** Service creates an internal virtual IP address.

Only resources inside the Kubernetes cluster can communicate with it.

Network flow:

```text
Browser
   ❌
   │
   │
ClusterIP Service
   │
   ▼
Nginx Pod
```

Since the browser is outside the cluster, it cannot directly reach the ClusterIP.

This is the expected behavior.

---

# What Happened When Running

```bash
minikube service nginx-service
```

Output:

```text
service has no node port
```

Reason:

The Service type was **ClusterIP**, so Kubernetes had not opened any external port on the Minikube node.

---

# Solution

To expose the application outside the cluster, the Service type was changed.

Two options are available.

---

# Option 1: NodePort (Recommended for Learning)

```yaml
spec:
  type: NodePort

  selector:
    app: nginx

  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
      nodePort: 30000
```

## How NodePort Works

Kubernetes opens a port (30000–32767) on every node.

Traffic flow:

```text
Browser
     │
     ▼
NodeIP:30000
     │
     ▼
NodePort Service
     │
     ▼
Pod
     │
     ▼
Nginx
```

Now the application can be accessed from outside the cluster.

---

# Option 2: LoadBalancer

```yaml
spec:
  type: LoadBalancer

  selector:
    app: nginx

  ports:
    - port: 80
      targetPort: 80
```

## How LoadBalancer Works

A cloud provider (or Minikube using `minikube tunnel`) creates an external load balancer.

Traffic flow:

```text
Internet
     │
     ▼
Load Balancer
     │
     ▼
Service
     │
     ▼
Pod
```

In Minikube, run:

```bash
minikube tunnel
```

to assign an external IP address.

---

# Why Was `nodePort` Added?

Example:

```yaml
nodePort: 30000
```

This reserves a fixed port on every Kubernetes node.

Instead of Kubernetes assigning a random NodePort, we explicitly chose `30000`.

Benefits:

* Easy to remember
* Useful for demonstrations
* Predictable external access

---

# Service Types Comparison

| Service Type | Internal Access | External Access             | Typical Use                                       |
| ------------ | --------------- | --------------------------- | ------------------------------------------------- |
| ClusterIP    | ✅               | ❌                           | Communication between services inside the cluster |
| NodePort     | ✅               | ✅                           | Learning, testing, development                    |
| LoadBalancer | ✅               | ✅                           | Production deployments on cloud platforms         |
| ExternalName | DNS only        | Depends on external service | Connecting to external resources                  |

---

# Architecture Comparison

## ClusterIP

```text
Browser
   ❌
   │
ClusterIP
   │
Pod
```

Only Pods inside the cluster can access the application.

---

## NodePort

```text
Browser
   │
NodeIP:30000
   │
NodePort Service
   │
Pod
```

Accessible from outside the cluster using the node's IP address and NodePort.

---

## LoadBalancer

```text
Internet
    │
Load Balancer
    │
Service
    │
Pod
```

Used in production environments where cloud providers automatically provision an external load balancer.

---

# Commands Used

Apply the configuration:

```bash
kubectl apply -f nginx-config.yaml
```

Check the Deployment:

```bash
kubectl get deployments
```

Check the Pods:

```bash
kubectl get pods
```

Check the Services:

```bash
kubectl get svc
```

Access a NodePort Service:

```bash
minikube service nginx-service
```

Access a LoadBalancer Service:

```bash
minikube tunnel
```

---

# Key Learning Points

* A Deployment manages Pods and keeps the desired number of replicas running.
* A Service provides a stable endpoint for communicating with Pods.
* `ClusterIP` exposes the application only within the Kubernetes cluster.
* `NodePort` exposes the application through a port on every Kubernetes node.
* `LoadBalancer` provides an external IP by integrating with a cloud load balancer or `minikube tunnel`.
* Labels and selectors connect Services to the correct Pods.
* Choosing the appropriate Service type depends on where the application needs to be accessible.

---

# Conclusion

The original configuration was functioning correctly; the limitation was not a configuration error but the chosen Service type. Since `ClusterIP` is intended for internal communication, it could not be reached from outside the cluster. By changing the Service type to `NodePort` or `LoadBalancer`, Kubernetes exposes the application externally while continuing to route requests to the Nginx Pod using label selectors.
