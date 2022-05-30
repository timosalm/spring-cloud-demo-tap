# Demo for a typical Spring Cloud Architecture on VMware Tanzu Application Platform


## Prerequisites
- TAP 1.2 installation
- The default installation of TAP uses a single Contour to provide internet-visible services. You can install a second Contour instance with service type ClusterIP if you want to expose some services to only the local cluster - which is recommended for this setup. The second instance must be installed in a separate namespace. You must set the CNR value `ingress.internal.namespace` to point to this namespace.
- RabbitMQ operator, Tanzu PostreSQL operator, Tanzu Gemfire operator, Tanzu Observability
- Queue Supply Chain (https://docs.vmware.com/en/VMware-Tanzu-Application-Platform/1.2/tap/GUID-workloads-queue.html)
- Customize supply chain for subpath support of the test step

## Setup

### Ops (should be automated and provided as self-service for prod env)

```
kubectl apply -f tap/ops/scan-policy.yaml -n <dev-namespace>
```
#### Config Server
```
kubectl create secret generic configserver-secret --from-literal=git-url=https://github.com/<user>/<config-repo>.git --from-literal=username=<git-username> --from-literal=password=<git-personal-access-token> -n <dev-namespace>
kubectl apply -f tap/ops/config-server.yaml -n <dev-namespace>
```

#### App SSO
For a TLS setup:
```
ytt -f tap/ops/auth-server-template.yaml -v dev_namespace=<dev-namespace> -v issuer_uri=https://authserver-1-<dev-namespace>.example.com -v tls_secret_name=<namespace>/<secret> | kubectl apply -f -
```

Otherwise:
```
ytt -f tap/ops/auth-server-template.yaml -v dev_namespace=<dev-namespace> -v issuer_uri=http://authserver-1-<dev-namespace>.example.com | kubectl apply -f -
```

#### Observability
```
ytt -f tap/ops/observability-template.yaml -v uri=https://vmwareprod.wavefront.com -v api_token=<token> | kubectl apply -n <dev-namespace> -f -
```

#### Gemfire
```
kubectl apply -f tap/ops/gemfire.yaml -n <dev-namespace>
```

#### PostgeSQL
```
kubectl apply -f tap/ops/postgres.yaml -n <dev-namespace>
```

#### RabbitMQ
```
kubectl apply -f tap/ops/rabbit.yaml -n <dev-namespace>
```

### Devs:
```
kubectl apply -f tap/test-pipeline.yaml -f -n <dev-namespace>
ytt -f tap/auth-client-template.yaml -v gateway_url=https://gateway-<dev-namespace>.cnr.example.com | kubectl apply -n <dev-namespace> -f -
kubectl apply -f tap/workload-product-service.yaml -n <dev-namespace>
kubectl apply -f tap/workload-order-service.yaml -n <dev-namespace>
kubectl apply -f tap/workload-shipping-service.yaml -n <dev-namespace>
kubectl apply -f tap/workload-shipping-service.yaml -n <dev-namespace>
```