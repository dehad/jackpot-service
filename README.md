# Jackpot Service

**Backend exercise implementation – Java 11 / Spring Boot 2.7 / Spring Kafka / H2 (in‑memory) / Spring Data JPA.**  The service exposes a small REST API for placing bets, contributes them to jackpot pools and evaluates them for rewards, exactly as described in the home‑assignment specification .

---

## 1. Tech stack

| Layer       | Technology                                     |
| ----------- | ---------------------------------------------- |
| Language    | Java 11                                        |
| Build       | Maven                                          |
| Framework   | Spring Boot 2.7.18                             |
| Persistence | Spring Data JPA + H2 (in‑memory)               |
| Messaging   | Spring Kafka (topic `jackpot-bets`)                        |
| Docs        | springdoc‑openapi → Swagger UI                 |
| Auth        | Custom **API‑key** filter (header `X‑API‑KEY`) |

---

## 2. Prerequisites

- **JDK 11+** (for bare‑metal run)
- **Docker & Docker Compose** – optional, but recommended for one‑command startup
- Local **Kafka** broker **OR** Docker Compose stack

---

## 3. Quick Start

### 3.1. Run with Docker Compose

```bash
# clone / unzip project root
cd jackpot-service

API_KEYS=<your-api-key> docker compose up --build        # Kafka + app in one shot
```

- App → http://localhost:8080
- Swagger UI → http://localhost:8080/swagger-ui.html
- H2 Console → http://localhost:8080/h2-console

### 3.2. Run locally (no containers)

```bash
mvn clean package
java -jar target/jackpot-service-0.0.1-SNAPSHOT.jar
```

If you **don’t** have Kafka running, messages will simply time‑out but the API will stay responsive.

---

## 4. Configuration

All tunables live in `src/main/resources/application.yml`.  Every property can be overridden through environment variables at runtime.

| Property                         | Default                                       | Purpose                                |
| -------------------------------- | --------------------------------------------- | -------------------------------------- |
| `API_KEYS`                       | `my-secret-key,another-secret`                | Comma‑separated list of valid API keys |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` (or `kafka:9092` in Compose) | Kafka broker URL                       |

```yaml
api:
  keys:
    valid: ${API_KEYS:my-secret-key,another-secret}
```

**Example:**

```bash
docker run -e API_KEYS="alphaKey,betaKey" -p 8080:8080 jackpot-service:latest
```

---

## 5. Database bootstrap

Spring Boot executes in order:

1. Creates the four tables (`BET`, `JACKPOT`, `JACKPOT_CONTRIBUTION`, `JACKPOT_REWARD`).
2. Loads three demo bets plus three demo jackpots:
   - `J1` – fixed contribution + fixed reward
   - `J2` – **variable contribution** (10 %, decays over time, divisor = 1000))
   - `J3` – fixed contribution + **variable reward** (0.5 %, divisor = 100, limit = 50 000)

---

## 6. REST API

| Method | Path                                   | Description                     | Auth                  |
| ------ | -------------------------------------- | ------------------------------- | --------------------- |
| POST   | `/bets`                                | Publish bet → Kafka             | **Yes** (`X-API-KEY`) |
| POST   | `/jackpots/rewards/evaluate-bet`       | Evaluate bet for jackpot reward | **Yes** (`X-API-KEY`)              |

### 6.1. Example cURL

```bash
API_KEY=my-secret-key

# place a bet
curl -H "X-API-KEY:$API_KEY" -H "Content-Type: application/json"      -d '{"userId":"U1","jackpotId":"J1","amount":10}'      http://localhost:8080/bets

# evaluate the bet
curl -H "X-API-KEY:$API_KEY" -H "Content-Type: application/json"      -d '{"betId":"{betId}"}'      http://localhost:8080/jackpots/rewards/evaluate-bet
```

Swagger UI provides interactive docs and payload schemas.

---

## 7. Project layout

```
jackpot-service/
 ├─ src/main/java/com/example/jackpot
 │   ├─ JackpotServiceApplication.java  # Spring Boot entrypoint
 │   ├─ controller/  # REST controllers
 │   ├─ service/     # business logic + Kafka listener
 │   ├─ model/       # JPA entities + enums
 │   ├─ dto/         # request / response DTOs
 │   ├─ repository/  # Spring‑Data repositories
 │   └─ config/      # Kafka, API‑key filter
 ├─ resources/
 │   ├─ application.yml
 │   └─ schema.sql / data.sql
 ├─ Dockerfile
 ├─ docker-compose.yml
 └─ pom.xml
```

---

## 8. Running tests

*Unit & integration stubs (JUnit 5, Kafka test containers).*
```bash
mvn test
```

---

## 9. Known limitations / Next steps

- No authentication beyond static API keys – it could be replaced with Spring Security + JWT.
- Rest API endpoints are limited to the purpose of task only
- In-memory H2 – production would use Postgres/MySQL.
- Error handling is minimal – it could be improved with custom exceptions and global error handlers.

---

