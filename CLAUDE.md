# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

SmartCare HMS — a Spring Boot 3.3.2 / Java 17 / MySQL 8 hospital management system. The Maven project lives in `smartcare-hms/`; the repo root only holds `docker-compose.yml`. There is a single deployable unit: the backend jar also serves the frontend (`src/main/resources/static/index.html`, a ~830-line vanilla HTML/JS single-page dashboard with no build step).

## Commands

All Maven commands run from `smartcare-hms/`:

```bash
mvn spring-boot:run            # run locally (needs MySQL on localhost:3306)
mvn clean package              # build target/smartcare-hms-1.0.0.jar
mvn clean package -DskipTests
mvn test                       # no test sources exist yet (src/test is absent)
mvn test -Dtest=ClassName#methodName   # single test, once tests are added
```

Full stack via Docker (from repo root):

```bash
docker compose up --build      # MySQL + backend, app on http://localhost:8080
docker compose down -v         # also drops the mysql_data volume / seed data
```

`docker-compose.yml` sets `DB_HOST=mysql-db`; `application.properties` falls back to `localhost` so the same build works both ways. `Dockerfile` is a two-stage build (maven:3.9.8-temurin-17 → eclipse-temurin:17-jre-alpine) and hardcodes the jar name `smartcare-hms-1.0.0.jar` — bumping `<version>` in `pom.xml` requires editing the Dockerfile too.

`smartcare-hms/target/` is currently committed/untracked in the tree; do not edit files under `target/classes/` — they are build output. Edit `src/main/resources/`.

## Architecture

Strict four-layer flow, one package per layer under `com.smartcare.hms`:

`controller` (`@RestController`, `/api/*`) → `service` (interface) → `service.impl` (`@Service`) → `repository` (`JpaRepository`) → `entity`.

Conventions that hold across the whole codebase:

- **Constructor injection only** — no `@Autowired` fields anywhere. Lombok is a dependency but entities are written with explicit hand-written getters/setters; follow the existing style rather than introducing `@Data`.
- **Every service has an interface** in `service/` and one impl in `service/impl/`. Add both when adding a feature.
- **Entities are the API contract.** There are no DTOs — controllers take and return JPA entities directly with `@Valid`. Validation constraints (`@NotBlank`, `@DecimalMin`, `@PositiveOrZero`) live on entity fields and are the primary input validation.
- **Nested entities arrive as bare IDs.** The frontend POSTs e.g. `{"patient": {"patientId": 3}, ...}`. Service impls must re-resolve those stubs from the repository before saving (see `resolveAndAttachPatientAndDoctor` in `AppointmentServiceImpl`, and the equivalent blocks in `AdmissionServiceImpl` / `BillServiceImpl`). Skipping this step causes detached-entity or null-column failures.
- **Every `@ManyToOne` is `FetchType.LAZY` and carries `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", ...})`.** This is what keeps entity-as-response-body serialization working; omitting it on a new relation reintroduces the proxy-serialization and circular-reference bugs (`Doctor.department` additionally ignores `headDoctor` to break the Doctor↔Department cycle). `spring.jpa.open-in-view=true` is deliberately on for the same reason.
- **`@Transactional` (Spring's, from `org.springframework.transaction.annotation`) on any multi-table write** — booking, admitting, discharging, billing.

### Domain model

`Person` is an abstract `@MappedSuperclass` (`fullName`, `contactNumber`, abstract `getRole()`); `Patient`, `Doctor`, and `Staff` extend it. Note the field is `fullName`, not `name` — the frontend was fixed to match this. Other entities: `Department`, `Room`, `Appointment`, `Admission`, `Treatment`, `LabTest`, `Bill`. Enums are nested in their entity (`Appointment.AppointmentStatus`, `Admission.AdmissionStatus`, `Bill.PaymentStatus`, `Bill.PaymentMethod`) and persisted with `@Enumerated(EnumType.STRING)`.

### Business rules living in the service layer

- `AppointmentServiceImpl` — rejects past dates and double-booking of a doctor at the same date+time (cancelled appointments don't count); cancelling flips status rather than deleting.
- `AdmissionServiceImpl` — refuses an unavailable room, and flips `Room.available` false on admit / true on discharge.
- `BillServiceImpl` — `Bill.recalculateTotal()` derives `totalAmount` from the four charge components; never trust a client-supplied total.
- `service.payment` — `PaymentService` interface with `Cash`/`Card`/`Online` impls selected at runtime by `PaymentServiceFactory.getPaymentService(String)` (a `switch` on `"CASH"|"CARD"|"ONLINE"`). Adding a method means: new impl + factory case + new `Bill.PaymentMethod` enum constant.
- `service.report` — `ReportService` interface, `RevenueReportService` impl.

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) maps the custom exceptions in `exception/` to a uniform `ErrorResponse` JSON body: `ResourceNotFoundException`→404, `AppointmentConflictException`/`RoomNotAvailableException`→409, `InvalidInputException`→400, `MethodArgumentNotValidException`→400 (aggregating field messages), anything else→500. Throw these instead of returning error `ResponseEntity`s from controllers.

### REST surface

`/api/patients`, `/api/doctors`, `/api/departments`, `/api/appointments`, `/api/admissions`, `/api/rooms`, `/api/treatments`, `/api/lab-tests`, `/api/bills`. Mostly standard CRUD, plus verb endpoints: `PUT /api/appointments/{id}/cancel`, `GET /api/appointments/doctor/{doctorId}/schedule`, `GET /api/bills/unpaid`, `PUT /api/bills/{id}/pay` (body `{"paymentMethod": "CARD"}`). The frontend calls these with a relative base (`const API = ''`), so it is same-origin only — no CORS config exists.

## Database

`ddl-auto=update` creates/evolves the schema, then `data.sql` seeds it (`defer-datasource-initialization=true` sequences these correctly). `data.sql` uses `INSERT IGNORE` with explicit primary keys so restarts are idempotent. When adding an entity, add matching seed rows there if the frontend needs data to render.

Credentials are hardcoded in `application.properties` and `docker-compose.yml` (root / `Adithya@2005`) — this is a coursework project; don't add new secrets, and don't silently rotate these without telling the user, since both files must stay in sync.

## Comments

Some service impls contain Sinhala-language comments (e.g. `AppointmentServiceImpl`). Leave them intact when editing nearby code; write new comments in English.
