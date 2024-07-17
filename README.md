# Daraja B2C API Integration Project

# Overview
This project demonstrates the integration of the M-Pesa Daraja B2C API using Spring Boot, Kafka, and MongoDB. The project handles B2C transactions by receiving requests, processing them, storing transaction details in MongoDB, and using Kafka for asynchronous communication between services.

## Prerequisites
- Java 11 or later
- Docker and Docker Compose

## Getting Started
## Clone the Repository
``
git@github.com:thabyte/Tada.git
``

``
cd Tada
``

run the command below to get db, kafka and zookeeper up and running
``
docker-compose up -d
``
```js
./gradlew bootRun
```