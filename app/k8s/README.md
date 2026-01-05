# Estruura de Pastas de Kubernetes
Esta pasta contém ficheiros e pastas relacionadas com a configuração e implantação dos clusters Kubernetes.

```
kubernetes/
├── namespaces/ # Configurações de namespaces para diferentes ambientes ou projetos
├── common/ # Configurações comuns compartilhadas entre diferentes clusters
├── Academins/ # Configurações e scripts específicos para o cluster Academins
├── Applications/
├── Enrollments
├── Schedules
├── AssementsPlanning
```



# Como correr o Minikube
Tem de se correr isto na pasta /app
```bash
minikube start --driver=docker
kubectl apply -R -f k8s/     (Pode ser preciso correr 2 vezes, uma para dar load dos namespaces)
kubectl port-forward svc/gateway -n gateway 8080:8080 &
```


# Dicionário de Comandos Úteis do Minikube e Kubectl
Ver estado do minikube, bom para quando não liga e não reparei:
```bash
minikube status
```

Começar a usar o Minikube com o driver Docker:
```bash
minikube start --driver=docker
```

Para usar o Docker do Minikube, acho que não é necessário porque as imagens veem do docker hub:
```bash
eval "$(minikube docker-env -u)"
```
Para parar o minikube:
```bash
minikube stop
```

Para ver o dashboard:
```bash
minikube dashboard
```

Para apagar tudo:
```bash
minikube delete --all
```

Ver namespcaces:
```bash
kubectl get namespaces
```

Ver pods a correr num namespace:
```bash
kubectl get pods -n <namespace>

para ver todos os namespaces:
kubectl get pods --all-namespaces
```

Ver logs de um pod em caso de erro:
```bash
kubectl logs <pod-name> -n <namespace>

pode ter um tail de 200 linhas por exemplo:
kubectl logs schedules-deployment-556846d5f8-2sbkm -n schedules --tail=200
```

Restart de um pod:
```bash
kubectl rollout restart deployment/<deployment-name> -n <namespace>
```

Ver url do serviço, como tudo tem ClusterIp, então este comando não serve de muito:
```bash
minikube service <service-name> -n <namespace> --url
```