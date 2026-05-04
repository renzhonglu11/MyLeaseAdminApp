# Project Memory

Generated from the current workspace on 2026-04-17.

This file is a repo-derived snapshot, not a durable assistant memory store. It reflects:

- Current source code structure
- Current local git state
- Visible in-progress implementation in the working tree

## Current repo state

- Git history is minimal: `f4d1f14 Initial project commit`
- There are uncommitted changes in the workspace. Do not assume all changes were made by the current assistant session.
- Recent deployment-related changes added a Docker Compose setup for the admin backend:
  - `docker-compose.web-admin.yml`
  - `web/web-admin/Dockerfile`
  - `.dockerignore`
  - `.env.web-admin.example`
- `web/web-admin/src/main/resources/application.yml` now reads DB, Redis, MinIO, and JWT values from environment variables while keeping local defaults.
- Admin `JwtUtils` now reads `jwt.secret` from configuration instead of using only a hardcoded signing key.
- The Docker Compose admin stack sets `SPRING_JPA_HIBERNATE_DDL_AUTO=none` by default because the sample schema is imported from `sql_scripts/lease.sql`; this avoids Hibernate schema-update attempts on production/startup.

## Project shape

This is a Gradle multi-project Spring Boot application named `lease`.

Modules from `settings.gradle`:

- `model`
- `common`
- `web`
- `web:web-admin`
- `web:web-app`

Shared build characteristics from `build.gradle`:

- Spring Boot `3.5.9`
- Java toolchain `21`
- Spring dependency management
- MySQL connector added as `runtimeOnly` for Spring Boot application modules
- Spring Boot test starter added for Spring Boot application modules
- Lombok
- MinIO
- JUnit 5

Current Gradle structure notes:

- `model` is now a `java-library` module rather than an `application`
- Gradle init-template leftovers were removed from `model` (`my_project.App` and its generated test)
- `common` and nested modules now rely more on root shared conventions instead of repeating repository/test boilerplate
- `web` is acting as an aggregator project for `web:web-admin` and `web:web-app`
- `web:web-app` now has explicit Boot app dependencies on `common`, `model`, and `spring-boot-starter-web`

## Entry points

- Admin app: `web/web-admin/src/main/java/com/rz/lease/web/WebAdminApplication.java`
  - Scans `com.rz.lease`
  - Uses JPA entity scan for `com.rz.lease.model.entity`
  - Enables custom base repository implementation for admin repositories
- App module: `web/web-app/src/main/java/com/rz/lease/WebAppApplication.java`

## App module package migration

- `web/web-app/src/main/java/com/rz/lease/web/app` has been normalized to the `com.rz.lease.web.app` package root.
- Stale `com.atguigu.lease...` imports in `web-app` Java sources were rewritten to `com.rz.lease...`.
- The old `web/web-app/src/main/java/web/app/WebAppApplication.java` entrypoint is deleted in favor of the `com.rz.lease` entrypoint.

## App module JPA migration

- `web:web-app` now uses Spring Data JPA like `web:web-admin`, not MyBatis-Plus.
- `web/web-app/build.gradle` includes `spring-boot-starter-data-jpa`, `spring-boot-starter-data-redis`, and Springdoc UI dependencies.
- `web:web-app` has local resource config for MySQL, Redis, MinIO, and JPA; JPA DDL generation is disabled with `spring.jpa.hibernate.ddl-auto=none`.
- App login code generation no longer uses Aliyun SMS. `LoginServiceImpl.sendCode(...)` stores the verification code in Redis and logs it for local development/testing.
- `WebAppApplication` now scans `com.rz.lease`, entity-scans `com.rz.lease.model.entity`, enables Spring Data page DTO serialization, and enables JPA repositories under `com.rz.lease.web.app.repository`.
- App mapper interfaces under `web/web-app/.../mapper` and generated MyBatis service implementations were removed from the compilation path.
- App services/controllers now use repository-backed JPA implementations and Spring Data `Page`/`Pageable` instead of MyBatis `IPage`, `Page`, wrappers, or `IService`.
- A small shared app login utility set was added under `common`:
  - `common.login.LoginUser`
  - `common.login.LoginUserHolder`
  - `common.utils.CodeUtil`
  - `common.utils.JwtUtil`
  - app Redis constants in `RedisConstant`
- `web:web-app` now uses Spring Security for app JWT authentication. `AppJwtAuthenticationFilter` parses `Authorization: Bearer ...` or `token` headers, stores the parsed user in both `SecurityContextHolder` and `LoginUserHolder`, and clears the thread-local after the request.
- `AppSecurityConfig` keeps public app browsing endpoints open (`/app/login`, `/app/login/getCode`, room/apartment/region/payment/term endpoints, and Swagger) while requiring authentication for user-specific endpoints such as `/app/info`, history, appointment, and agreement APIs.
- App Swagger/OpenAPI now declares a bearer JWT security scheme, so Swagger UI has an Authorize control for setting the app token.
- App room detail retrieval remains public; it can use Redis room-detail caching via `app.room-detail-cache.enabled` and saves browsing history only when a login user is present, avoiding anonymous-request NPEs.
- Verification: `./gradlew :web:web-app:compileJava` succeeds, and `./gradlew :web:web-app:bootRun` starts successfully on port `8081` with the `local` profile. Remaining compile output is Lombok superclass `equals/hashCode` warnings on VO classes.

## Domain understanding

The project models a rental / lease management system. Confirmed domain objects include:

- Apartment and room inventory
- Lease agreements and lease terms
- Facilities, labels, attributes, fee keys / fee values
- Browsing history and viewing appointments
- System users and posts
- Region hierarchy: province, city, district
- Release / lease / appointment lifecycle enums

The `model` module contains JPA entities and enums. The `web-admin` module contains controllers, services, repositories, and admin-facing view objects.

## Apartment admin flow

Relevant files currently in focus:

- `model/src/main/java/com/rz/lease/model/entity/ApartmentInfo.java`
- `web/web-admin/src/main/java/com/rz/lease/web/admin/controller/apartment/ApartmentController.java`
- `web/web-admin/src/main/java/com/rz/lease/web/admin/service/impl/ApartmentInfoServiceImpl.java`
- `web/web-admin/src/main/java/com/rz/lease/web/admin/vo/apartment/ApartmentItemVo.java`
- `web/web-admin/src/main/java/com/rz/lease/web/admin/vo/apartment/ApartmentQueryVo.java`

Confirmed behavior:

- `ApartmentController.pageItem(...)` exposes paginated apartment list querying
- `ApartmentInfoServiceImpl.pageItem(...)`:
  - Builds a pageable request sorted by descending `id`
  - Filters apartments by `provinceId`, `cityId`, and `districtId`
  - Loads rooms for the current apartment page
  - Computes:
    - `totalRoomCount`
    - `freeRoomCount`
  - Maps entity data into `ApartmentItemVo`
- `saveOrUpdateApartmentInfo(...)` persists apartment base info and rewrites related facility, label, fee, and graph relations

Free-room calculation logic currently treats these lease statuses as occupied:

- `SIGNING`
- `SIGNED`
- `WITHDRAWING`
- `RENEWING`

## Visible in-progress implementation

6. Admin login authentication has moved toward Spring Security primitives:

- `web/web-admin` now includes Spring Security starter and a baseline `SecurityConfig`.
- `LoginServiceImpl.login(...)` now uses `AuthenticationManager` for username/password authentication after captcha verification.
- Password storage in `SystemUserServiceImpl.saveOrUpdate(...)` now uses `BCrypt` via `PasswordEncoder` instead of MD5.
- Added `AdminUserDetailsService` to load active users from `SystemUserRepository` for security authentication.
- `common` `jwtUtils` is now a Spring bean (`@Component`) and is used by login to issue JWTs.
- Added custom admin principal `AdminUserPrincipal` (`UserDetails`) carrying `id`; `AdminUserDetailsService` now returns it.
- `/admin/info` now reads current user id from this principal and delegates to `SystemUserService.getUserInfoById(...)` instead of `UserInfoService`.

Current caveat:

- Existing DB users still stored with MD5 hashes will fail AuthenticationManager password checks until migrated to BCrypt.

From the current uncommitted diff:

1. `ApartmentItemVo` was changed from extending `ApartmentInfo` to being an explicit standalone VO.
   This is a good direction because it decouples API response shape from the JPA entity and leaves room for computed fields like room counts.

2. `ApartmentInfoServiceImpl` currently has added logging around apartment room-count calculation:

- logs `Apartment {id}: totalRoomCount={...}, occupiedRoomCount={...}`

3. `ApartmentItemVo` currently contains these response fields:

- Base apartment info copied from `ApartmentInfo`
- `totalRoomCount`
- `freeRoomCount`

4. `RoomController` and the room service layer are now implemented to mirror the apartment admin API surface:

- `RoomController` delegates all endpoints to `RoomInfoService`
- `RoomInfoService` now defines room CRUD/detail/list/release methods
- `RoomInfoServiceImpl` now handles:
  - room save/update
  - paged room querying with apartment-region filters
  - room detail aggregation for graphs, attributes, facilities, labels, payment types, and lease terms
  - room delete and release-status updates
  - basic room listing by apartment id

5. JSON serialization is now hardened against lazy JPA proxy failures for currently exposed admin responses:

- lazy many-to-one associations on `RoomInfo`, `ApartmentInfo`, `LeaseAgreement`, and `SystemUser` are `@JsonIgnore`
- `WebAdminApplication` enables Spring Data page serialization mode `VIA_DTO` to avoid `PageImpl` warning logs

6. `SystemUserServiceImpl.page(...)` is now implemented:

- paginates by `id` ascending
- filters by `name` and `phone`
- maps `SystemUser` rows into `SystemUserItemVo`
- enriches each row with `postName` via `SystemPostRepository`

## Known gaps visible from current code

`ApartmentController` has multiple endpoints that currently return empty `Result.ok()` placeholders and appear unfinished:

- `getDetailById`
- `removeById`
- `updateReleaseStatusById`
- `listInfoByDistrictId`

That suggests the apartment list flow is further along than the rest of the apartment admin CRUD surface.

## Practical memory summary

If I continue working in this repo, the safest assumptions are:

- Keep entity classes in `model`
- Keep admin request/response VOs in `web/web-admin/.../vo/...`
- Keep business aggregation in `...service.impl...`
- Avoid exposing entities directly in admin list responses when computed fields are involved
- Apartment list work currently centers on paginated filtering plus derived room availability counts

## Limitations

I cannot reconstruct a true historical list of "everything implemented until now" from assistant memory because this repo only shows:

- one initial commit
- the current working tree

If you want, I can also generate a second markdown file that is narrower and more actionable, for example:

- `IMPLEMENTATION_LOG.md` from current git diff only
- `ARCHITECTURE_NOTES.md` from repo structure only
- `APARTMENT_MODULE_MEMORY.md` focused just on the apartment flow
