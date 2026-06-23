## Pod vs Deployment

### Pod

A Pod is the smallest deployable unit in Kubernetes and contains one or more containers.

#### Container Self-Healing

If the application inside the container crashes or exits (for example, by calling `System.exit()` or Spring Boot shutdown), Kubernetes restarts the **container** because the default `restartPolicy` is `Always`.

```
Pod
 └── Container (Spring Boot)
        ↓
   Application exits
        ↓
   Container restarts
```

> **Note:** The Pod itself is not recreated. Only the container inside the Pod is restarted.

---

#### Pod Deletion

A standalone Pod is **not self-healing**.

If you manually delete it:

```bash
kubectl delete pod backend
```

The Pod is permanently removed because no controller exists to recreate it.

---

#### Image Update

A standalone Pod does **not** support updating the container image.

For example, you cannot change:

```
shaad98/k8s-demo:v1
```

to

```
shaad98/k8s-demo:v2
```

You must delete the Pod and create a new one.

---

### Deployment

A Deployment manages Pods using a ReplicaSet.

#### Pod Self-Healing

If a Pod is deleted or becomes unavailable:

```bash
kubectl delete pod backend-xxxxx
```

The Deployment automatically creates a **new Pod** to maintain the desired number of replicas.

```
Deployment
      │
 ReplicaSet
      │
    Pod deleted
      │
      ▼
 New Pod created
```

---

#### Scaling

A Deployment can run multiple replicas.

```yaml
replicas: 3
```

If one Pod fails or is deleted, Kubernetes automatically creates another Pod to maintain three running replicas.

---

#### Rolling Updates

Deployment supports updating the application image without downtime.

```bash
kubectl set image deployment/backend \
backend=shaad98/k8s-demo:v2
```

Kubernetes creates new Pods with the updated image and gradually removes the old Pods.

---

## Key Differences

| Feature | Pod | Deployment |
|---------|-----|------------|
| Restarts crashed container | ✅ Yes | ✅ Yes |
| Recreates deleted Pod | ❌ No | ✅ Yes |
| Supports replicas | ❌ No | ✅ Yes |
| Rolling updates | ❌ No | ✅ Yes |
| Rollback support | ❌ No | ✅ Yes |
| Image update | ❌ Delete & recreate Pod | ✅ Update Deployment |

---

## Summary

- A **container** can crash, and the **Pod** restarts the container.
- A **Pod** does not recreate itself if it is deleted.
- A **Deployment** recreates deleted Pods automatically.
- A **Deployment** manages replicas, rolling updates, and rollbacks.
- In production environments, applications are typically deployed using **Deployments** rather than standalone Pods.