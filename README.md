# Librarian API
[OpenAPI Documentation](http://localhost:8080/swagger-ui/index.html) - When API is running.

## Dependencies
- openjdk:20
- Docker Desktop install (and logged in)
  - K8s
  - thomseno/h2 (ARM64)
- Helm (used Homebrew for dependencies on Mac OSX)
- kubectl (controls the Kubernetes cluster manager)

## Running Locally

**Build:**
```bash
./gradlew clean build
```

**Run via embedded Tomcat server provided by Spring Boot:**
```bash
./gradlew clean build bootRun
```

## Running with Docker

**Build:**
```bash
./gradlew clean build
```

**Run:**
```bash
docker-compose up
```

## Deploy with k8s

**Login:**
```bash
docker login
```

**Build:**
```bash
docker build -t jdoconnell/librarian-api:1.0.0 .
```

**Push to Docker Hub:**
```bash
docker push jdoconnell/librarian-api:1.0.0
```

**Install helm charts:**
```bash
helm install librarianapi-release ./helm
```
> You should now see K8s Pod/Application container(s) on your Docker desktop or via CLI

**Check your pods/deployment/services:**
```bash
kubectl get pods,deployments,services -n default
```
> Check out more kubectl commands you can use to manage your deployment (i.e. cluster-info, describe, logs, get deployments).

**Test the Kubernetes service:**
```bash
curl -v http://localhost:32312/books | json_pp
```
> Note: We're now pointing at the NodePort 32312 as defined in our helm service.yaml.

**Safely bring down deployment when ready:**
```bash
helm uninstall librarianapi-release
```

## Basic Functional Tests

`GET /books` -> Retrieve all books.
```bash
curl -v http://localhost:8080/books | json_pp
```

`GET /books/{id}` -> Retrieve a book by id.
```bash
curl -v http://localhost:8080/books/2 | json_pp
```

`POST /books` -> Insert a new book.
```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"author": "J.R.R. Tolkien", "title": "The Two Towers"}' \
  http://localhost:8080/books
```

`PUT /books/{id}` -> Update an existing book by id.
```bash
curl -X PUT \
  -H "Content-Type: application/json" \
  -d '{"title": "The Fellowship of the Ring"}' \
  http://localhost:8080/books/1
```

`DELETE /books/{id}` -> Remove an existing book by id.
```bash
curl -X DELETE http://localhost:8080/books/1
```

## Contributing
[James O'Connell](jdo.info@pm.me) - Software Engineer