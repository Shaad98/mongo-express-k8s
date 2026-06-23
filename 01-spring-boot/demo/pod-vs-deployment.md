# Running a Single Pod vs Deployment

## Run a Single Pod

Create a standalone Pod:

```bash
kubectl run backend \
  --image=shaad98/k8s-demo:v3 \
  --port=8080
```

Verify the Pod:

```bash
kubectl get pods
```

Describe the Pod:

```bash
kubectl describe pod backend
```

View Pod logs:

```bash
kubectl logs backend
```

Delete the Pod:

```bash
kubectl delete pod backend
```

---

# Standalone Pod vs Deployment

| Standalone Pod                                             | Deployment                                                      |
| ---------------------------------------------------------- | --------------------------------------------------------------- |
| Created using `kubectl run`                                | Created using `kubectl create deployment` or a Deployment YAML  |
| Creates only **one Pod**                                   | Manages one or more Pods through a ReplicaSet                   |
| Cannot be scaled                                           | Can be scaled to multiple replicas                              |
| No ReplicaSet is created                                   | ReplicaSet is created automatically                             |
| If the Pod is deleted, Kubernetes does **not** recreate it | If a Pod is deleted, Kubernetes automatically creates a new one |
| Suitable for testing and learning                          | Suitable for production workloads                               |
| No rolling updates                                         | Supports rolling updates and rollbacks                          |
| No self-healing                                            | Provides self-healing by recreating failed Pods                 |

---

## Deployment Object Hierarchy

```
Deployment
    │
ReplicaSet
    │
Pods
```

A Deployment manages a ReplicaSet, and the ReplicaSet manages the Pods.

---

## Standalone Pod Hierarchy

```
Pod
```

The Pod is created directly and is not managed by any higher-level Kubernetes object.

---

## Can a Standalone Pod Have Multiple Replicas?

**No.**

A Pod created using:

```bash
kubectl run backend --image=shaad98/k8s-demo:v3
```

creates exactly **one Pod**.

It cannot be scaled because it is **not managed by a Deployment or ReplicaSet**.

To run multiple replicas, create a Deployment:

```bash
kubectl create deployment backend --image=shaad98/k8s-demo:v3
```

Scale the Deployment:

```bash
kubectl scale deployment backend --replicas=3
```

Verify:

```bash
kubectl get deployments
kubectl get rs
kubectl get pods
```

The Deployment creates a ReplicaSet, and the ReplicaSet creates the required number of Pods.
