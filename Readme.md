# Checkout App

Simple checkout app built using Spring boot framework.
Application is built in a multistaged docker build 
and hence can be both build and run by running docker build and docker run.

For building fatjar a docker file that compatible with M2 processor is used, i.e. using
ARM64 architecture. If you are experiencing issues during docker build process replace
the builder base image with
```Dockerfile
FROM gradle:7.6.0-jdk17 as builder
```

## Build
(Only necessary if changes to code are made after running docker-compose -d up)
```bash
docker-compose build
```

## Run
Docker compose is used for spinning up the db (Postgresql) and spring boot app.
```bash
docker-compose up -d
```

Invoke checkout endpoint
```bash
curl -X POST -H "Content-Type: application/json" \                                                                                          7 ↵
    -d '["001","001", "002", "001", "004","003"]' \
    localhost:8080/checkout

```

## Further improvements
-- Add e2e test(s)
-- Add integration test(s)
