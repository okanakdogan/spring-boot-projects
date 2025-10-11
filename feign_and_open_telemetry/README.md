# 🔗 Microservices Communication with Feign & OpenTelemetry

This project demonstrates **microservices communication** using **Spring Cloud OpenFeign**, **Netflix Eureka** for service discovery, and **OpenTelemetry** for distributed tracing. It showcases how to build resilient, observable distributed systems with proper service-to-service communication patterns and comprehensive tracing.

---

## 💡 What This Project Demonstrates

This project implements a **microservices architecture** with:

1. 🏛️ **Eureka Server** — Service discovery and registration
2. 🔗 **Service 1** — Client service that calls other services
3. 🎯 **Service 2** — Target service that provides business logic
4. 📡 **OpenFeign** — Declarative HTTP client for service communication
5. 🔍 **Zipkin** — Distributed tracing and observability
6. 📊 **OpenTelemetry** — Modern observability framework

The architecture demonstrates how modern microservices handle:
- **Service Discovery** (finding services automatically)
- **Load Balancing** (distributing requests)
- **Fault Tolerance** (handling failures gracefully)
- **Inter-service Communication** (service-to-service calls)
- **Distributed Tracing** (end-to-end request tracking)
- **Observability** (monitoring and debugging)

---

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Eureka Server │    │   Service 1     │    │   Service 2     │    │     Zipkin      │
│   (Port: 8761)  │    │   (Port: 8080)  │    │   (Port: 8081)  │    │   (Port: 9411)  │
│                 │    │                 │    │                 │    │                 │
│ • Service       │◄───┤ • Feign Client  │───►│ • REST API      │    │ • Distributed    │
│   Registry      │    │ • Service       │    │ • Business      │    │   Tracing       │
│ • Health Check  │    │   Discovery     │    │   Logic         │    │ • Observability  │
│                 │    │ • OpenTelemetry │    │ • OpenTelemetry │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │                       ▲
                                └───────────────────────┼───────────────────────┘
                                                        │
                                                📊 Traces & Spans
```

---

## 🛠️ How It Works

### 1. 🏛️ Eureka Server – The Service Registry

* **What it does:** Acts as a central registry where all services register themselves and discover other services.
* **Why it matters:** Services don't need to know hardcoded URLs—they can find each other dynamically.
* **Key features:**
  * Service registration and discovery
  * Health monitoring
  * Load balancing support

### 2. 🔗 Service 1 – The Feign Client

* **What it does:** Demonstrates how to call other services using OpenFeign.
* **Key features:**
  * Declarative HTTP client (no boilerplate code)
  * Automatic service discovery
  * Built-in load balancing
  * Fault tolerance

### 3. 🎯 Service 2 – The Business Service

* **What it does:** Provides business logic and responds to service calls.
* **Key features:**
  * RESTful API endpoints
  * Health check endpoints
  * Service registration with Eureka
  * Custom tracing spans for business operations

### 4. 🔍 Zipkin – Distributed Tracing

* **What it does:** Collects, stores, and visualizes distributed traces from all services.
* **Key features:**
  * End-to-end request tracking
  * Service dependency mapping
  * Performance analysis
  * Error debugging

### 5. 📊 OpenTelemetry – Observability Framework

* **What it does:** Provides standardized observability across all services.
* **Key features:**
  * Automatic instrumentation
  * Custom span creation
  * Trace correlation
  * Metrics and logging integration

---

## 🔄 Service Communication Flow

Here's what happens when Service 1 calls Service 2:

1. 🚀 **Service 1** receives a request to call Service 2
2. 📊 **OpenTelemetry** creates a root span for the request
3. 🔍 **OpenFeign** asks Eureka: "Where is Service 2?"
4. 📍 **Eureka** responds with Service 2's location(s)
5. 📡 **OpenFeign** makes HTTP call to Service 2 (with trace context)
6. 📊 **Service 2** creates child spans for business operations
7. ✅ **Service 2** processes request and returns response
8. 📊 **Traces** are sent to Zipkin for visualization
9. 🔄 **Service 1** returns the result to the client

**Benefits:**
- 🎯 **No hardcoded URLs** — Services find each other automatically
- ⚖️ **Load balancing** — Requests distributed across multiple instances
- 🛡️ **Fault tolerance** — Automatic retry and circuit breaking
- 📊 **Monitoring** — Built-in metrics and health checks
- 🔍 **Distributed Tracing** — End-to-end request visibility
- 🐛 **Debugging** — Easy identification of bottlenecks and errors

---

## 🧰 Technologies Used

| Technology | Purpose |
|------------|--------|
| **Java 21** | Core language |
| **Spring Boot 3.5.6** | Application framework |
| **Spring Cloud OpenFeign** | Declarative HTTP client |
| **Netflix Eureka** | Service discovery |
| **OpenTelemetry** | Distributed tracing framework |
| **Zipkin** | Trace collection and visualization |
| **Micrometer** | Application metrics and tracing |
| **Docker** | Containerization |
| **Docker Compose** | Multi-service orchestration |

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose

### Quick Start

1. **Start all services:**
   ```bash
   docker-compose up --build
   ```

2. **Verify services are running:**
   - Eureka Dashboard: http://localhost:8761
   - Service 1: http://localhost:8080
   - Service 2: http://localhost:8081
   - Zipkin UI: http://localhost:9411

### API Endpoints

**Service 1 (Port 8080):**
- `GET /api/hello/{name}` — Direct hello from Service 1
- `GET /api/call-service2/{name}` — Service 1 calls Service 2
- `GET /api/service2-status` — Check Service 2 health
- `GET /api/process-order/{orderId}` — Process order with distributed tracing

**Service 2 (Port 8081):**
- `GET /api/hello/{name}` — Direct hello from Service 2
- `GET /api/status` — Service 2 health check
- `GET /api/validate-order/{orderId}` — Validate order with custom spans

### Example Usage

```bash
# Direct call to Service 1
curl http://localhost:8080/api/hello/World

# Service 1 calling Service 2 via Feign
curl http://localhost:8080/api/call-service2/World

# Check Service 2 status through Service 1
curl http://localhost:8080/api/service2-status

# Process order with distributed tracing (creates spans in Zipkin)
curl http://localhost:8080/api/process-order/12345

# Direct call to Service 2
curl http://localhost:8081/api/hello/World

# Validate order directly on Service 2
curl http://localhost:8081/api/validate-order/12345
```

---

## 🔧 Configuration

### Service Discovery
All services register with Eureka server at `http://eureka-server:8761/eureka/`

### Feign Client Configuration
```java
@FeignClient(name = "SERVICE-2")
public interface Service2Client {
    @GetMapping("/api/hello/{name}")
    String sayHello(@PathVariable("name") String name);
    
    @GetMapping("/api/status")
    String getStatus();
    
    @GetMapping("/api/validate-order/{orderId}")
    String validateOrder(@PathVariable("orderId") String orderId);
}
```

### OpenTelemetry Configuration
```properties
# Enable tracing
management.tracing.enabled=true
management.tracing.sampling.probability=1.0

# Zipkin exporter
management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans

# Service name for tracing
management.observations.key-values.service.name=service-1
```

### Docker Networking
Services communicate through the `eureka-network` bridge network, allowing them to find each other by service name.

---

## 📊 Monitoring & Health Checks

- **Eureka Dashboard:** http://localhost:8761
- **Zipkin UI:** http://localhost:9411
- **Service Health:** Each service exposes `/actuator/health` endpoint
- **Service Discovery:** View registered services in Eureka dashboard
- **Distributed Tracing:** View traces and spans in Zipkin UI
- **Custom Spans:** Business operations create detailed tracing spans

---

## 🎯 Key Learning Points

1. **Service Discovery:** How services find each other without hardcoded URLs
2. **Declarative HTTP Clients:** OpenFeign eliminates boilerplate HTTP client code
3. **Microservices Communication:** Best practices for service-to-service calls
4. **Container Orchestration:** Docker Compose for multi-service development
5. **Fault Tolerance:** Built-in retry and circuit breaking capabilities
6. **Distributed Tracing:** End-to-end request tracking across services
7. **Observability:** Custom spans and trace correlation
8. **OpenTelemetry Integration:** Modern observability standards

---

## 🔮 Future Enhancements

- **Circuit Breaker** — Hystrix or Resilience4j integration
- **API Gateway** — Spring Cloud Gateway for routing
- **Configuration Server** — Centralized configuration management
- **Message Queues** — Asynchronous communication patterns
- **Metrics Collection** — Prometheus and Grafana integration
- **Log Aggregation** — ELK Stack or similar
- **Service Mesh** — Istio integration for advanced traffic management

---

## 📚 References

- [Spring Cloud OpenFeign Documentation](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
- [Netflix Eureka Documentation](https://github.com/Netflix/eureka)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [OpenTelemetry Documentation](https://opentelemetry.io/docs/)
- [Zipkin Documentation](https://zipkin.io/)
- [Micrometer Tracing Documentation](https://micrometer.io/docs/tracing)
