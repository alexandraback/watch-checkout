# Checkout App

Simple checkout app built using Spring boot framework.
Application is built in a multi staged docker build 
and hence can be both build and run by running from root folder.
```bash
docker build -t app .
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