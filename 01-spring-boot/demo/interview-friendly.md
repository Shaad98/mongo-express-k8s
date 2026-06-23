# Kubernetes Components Summary

## Container

A container is the runtime environment that executes your application.

* Runs the application (e.g., Spring Boot).
* Can crash or exit.
* If the application exits, the container stops.

---

## Pod

A Pod is the smallest deployable unit in Kubernetes and can contain one or more containers.

* Hosts one or more containers.
* Restarts crashed or exited containers (default `restartPolicy: Always`).
* Does **not** recreate itself if it is deleted.
* Does **not** support rolling image updates.
* To update the image of a standalone Pod, delete the Pod and create a new one.

---

## Deployment

A Deployment is a Kubernetes controller that manages Pods.

* Manages Pods.
* Recreates deleted Pods automatically (self-healing).
* Supports multiple replicas (scaling).
* Supports rolling updates with zero or minimal downtime.
* Supports rollback to a previous version.
* Supports updating the container image without manually recreating Pods.

---

## Lifecycle Overview

```text
Application
      ↓
Container
      ↓
Pod (manages container lifecycle)
      ↓
Deployment (manages Pod lifecycle)
```

### Key Takeaways

* **Container** → Runs the application and can crash or exit.
* **Pod** → Manages container lifecycle and restarts failed containers.
* **Deployment** → Manages Pod lifecycle, provides self-healing, scaling, rolling updates, and rollbacks.
