# Project Memory

Generated from the current workspace on 2026-04-17.

This file is a repo-derived snapshot, not a durable assistant memory store. It reflects:

- Current source code structure
- Current local git state
- Visible in-progress implementation in the working tree

## Current repo state

- Git history is minimal: `f4d1f14 Initial project commit`
- There are 2 uncommitted Java changes visible right now:
  - `web/web-admin/src/main/java/com/rz/lease/web/admin/service/impl/ApartmentInfoServiceImpl.java`
  - `web/web-admin/src/main/java/com/rz/lease/web/admin/vo/apartment/ApartmentItemVo.java`

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
- App module: `web/web-app/src/main/java/web/app/WebAppApplication.java`

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
