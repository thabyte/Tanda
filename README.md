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

To run all tests using Gradle:

```js
./gradlew test
```

## API Endpoints


### Request B2C Payment
   Endpoint: POST /api/v1/b2c/request

* Description: This endpoint handles B2C payment requests.

* Request Body:

```json
{
  "Amount": 13000,
  "MobileNumber": "254708374149"
}
```

* Response:

* Success:
```json
{
  "id": "UUID",
  "status": "Success",
  "ref": "Unknown"
}

```

* Failure:
```json
{
  "id": "UUID",
  "status": "Failed",
  "ref": "Unknown"
}
```

### Get Payment Status
* Endpoint: GET /status/{transactionId}

* Description: Retrieves the payment status for a given transaction ID.

* Response:

- 200 OK with PaymentStatus in the response body if found.
- 404 Not Found if the transaction ID does not exist.


### Update Payment Status
* Endpoint: PUT /update

* Description: Updates the payment status.

Request Body:

```json
{
  "transactionID": "string",
  "status": "string",
  "details": "string"
}
```






