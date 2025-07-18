# Authorization Service

A lightweight Spring Boot web service that acts as an authorization façade.

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps to run the service
```
mvn clean install
mvn spring-boot:run
```

Service will start on `http://localhost:8080`.

## Sample Request

### POST /authorize
```json
{
  "userId": "123",
  "action": "read",
  "resource": "document-456"
}
```

### curl
```
curl -v -X POST http://localhost:8080/authorize -H "Content-Type: application/json" -d "{\"userId\":\"123\",\"action\":\"write\",\"resource\":\"document-456\"}"### Response
```

### Response
```json
{
  "authorized": true
}
```

## Notes
- Downstream service mocked using in-memory map; entries are userid, action, resource
- Controller times out after 2 seconds (hardcoded in the class for now), returns HTTP 504. This can be simulated by setting the below value to > 2000 in the service class:
###
```
// Simulate delay for timeout testing
// AuthorizationController sets timeout to 2 seconds; when this hardcoded
// value > 2000, controller will time out
try { Thread.sleep(500); } catch (InterruptedException ignored) {}

```
- Controller returns HTTP 500 when downstream service encounters an error.
- Thread pool configured for use at the service layer
- Input validation performed
- Global error handling implemented for couple of error types (should add more)
- Junit tests - INCOMPLETE - I pushed what I have so far, but they do not pass.
- "Production-ready" means the application is stable, secure, and designed to run reliably under real-world conditions. It includes proper error handling, observability (logging and metrics), concurrency support, and is resilient to failures like timeouts or crashes in downstream systems.
- Total time spent = 4 hours

## TODOs

- circuit breaker (e.g. Resilience4j)
- rate limiting
- unit and integration tests