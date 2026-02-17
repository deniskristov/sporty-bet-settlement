# SportyBet Settlement Service
[![Java build with Maven](https://github.com/deniskristov/sporty-bet-settlement/actions/workflows/test.yml/badge.svg)](https://github.com/deniskristov/sporty-bet-settlement/actions/workflows/test.yml)


A multi-module Spring Boot application that provides an API endpoint to publish sport events to Kafka and a consumer service to process them, built with clean architecture principles.  
See `Requirements.pdf` for the details.
## Requirements

- Docker and Docker Compose
- Java 17 (if running locally without Docker)
- Maven 3.9+ (if running locally without Docker)

## Running the Application

### Using Docker Compose (Recommended)

Start all services (Zookeeper, Kafka, Publisher, and Consumer):

```bash
docker-compose up --build
```

This will:
1. Start Zookeeper on port 2181
2. Start Kafka on port 9092
3. Start RocketMQ NameServer on port 9876
4. Start RocketMQ Broker on ports 10909, 10911, 10912
5. Build and start the Event Publisher service on port 8080
6. Build and start the Event Consumer service on port 8081
7. Build and start the Betting Settlement service on port 8082

### Running Locally

1. Start Kafka and Zookeeper:
```bash
docker-compose up zookeeper kafka
```

2. Run the event-publisher:
```bash
cd event-publisher
mvn spring-boot:run
```

3. Run the event-consumer (in a separate terminal):
```bash
cd event-consumer
mvn spring-boot:run
```

4. Run the betting-settlement-service (in a separate terminal):
```bash
cd betting-settlement-service
mvn spring-boot:run
```

## API Usage

### Authentication

The Publisher API is protected with HTTP Basic Authentication:
- **Username:** `sporty`
- **Password:** `sporty`

### Create Sport Event

**Endpoint:** `POST /api/v1/sport-events`

**Authentication:** Basic Auth (sporty/sporty)

**Request Body:**
```json
{
  "eventId": 1,
  "eventName": "Manchester United vs Liverpool",
  "eventWinnerId": 67890
}
```

**Response (201 Created):**
```json
{
  "eventId": 1,
  "eventName": "Manchester United vs Liverpool",
  "eventWinnerId": 67890,
  "status": "PUBLISHED"
}
```

### Example cURL Command

```bash
curl -X POST http://localhost:8080/api/v1/sport-events \
  -u sporty:sporty \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "eventName": "Manchester United vs Liverpool",
    "eventWinnerId": 67890
  }'
```

**Response for Unauthorized Access (401):**
```json
{
  "timestamp": "2024-02-17T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/v1/sport-events"
}
```

## Consumer Database

The Consumer service uses an H2 in-memory database that is initialized on startup with 10 test bets:

| betId | userId | eventId | eventMarketId | eventWinnerId | betAmount |
|-------|--------|---------|---------------|---------------|-----------|
| BET-0001 | 1001 | 1 | 2001 | 3001 | 100 |
| BET-0002 | 1002 | 2 | 2002 | 3002 | 200 |
| BET-0003 | 1003 | 3 | 2003 | 3003 | 300 |
| ... | ... | ... | ... | ... | ... |
| BET-0010 | 1010 | 10 | 2010 | 3010 | 1000 |

When a sport event is consumed, the service checks for bets with matching eventId and logs them.