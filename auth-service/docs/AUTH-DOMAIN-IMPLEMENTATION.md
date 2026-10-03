# Auth Service — Domain & Persistence Implementation Guide

## 1. Mục đích

Thiết kế và triển khai **Auth Service** theo hướng:

- DDD
- Clean Architecture
- Domain-driven design
- Spring Boot
- PostgreSQL
- JPA / Hibernate
- Flyway
- Maven

Mục tiêu của phase này **không phải viết full authentication system**.

Mục tiêu là xây dựng đúng:

```text
Database
    ↓
Domain Model
    ↓
Repository Abstraction
    ↓
JPA Persistence
    ↓
PostgreSQL
```

Code phải được thiết kế theo hướng **learning-oriented**:

> Codex tạo architecture, skeleton, interface, class và TODO; developer tự hoàn thiện phần implementation quan trọng.

---

# 2. Nguyên tắc quan trọng

## 2.1 Không code full

Codex **KHÔNG được viết hoàn chỉnh toàn bộ business logic**.

Codex được phép tạo:

- package structure
- class
- interface
- field
- constructor skeleton
- method signature
- JPA entity skeleton
- repository skeleton
- mapper skeleton
- TODO
- comment hướng dẫn

Nhưng các phần quan trọng phải để developer tự implement.

Ví dụ:

```java
public final class UserId {

    private final Long value;

    // TODO:
    // Implement constructor hoặc factory method.
    //
    // Yêu cầu:
    // - value không được null
    // - UserId phải immutable
    // - không có setter
    // - xác định validation phù hợp

    // TODO:
    // Implement accessor.

    // TODO:
    // Implement equals().

    // TODO:
    // Implement hashCode().
}
```

Không tự động hoàn thiện toàn bộ TODO.

---

# 3. Technology Stack

Auth Service sử dụng:

```text
Java
Spring Boot
Spring Data JPA
Hibernate
PostgreSQL
Flyway
Maven
```

Persistence flow:

```text
Domain
   ↓
Repository Interface
   ↓
Repository Implementation
   ↓
JPA Entity
   ↓
Spring Data JPA / Hibernate
   ↓
PostgreSQL
```

---

# 4. Database Schema

Database:

```text
auth_db
```

Schema ban đầu:

```text
auth_db
│
├── users
│
└── refresh_tokens
```

Database sử dụng:

```text
PostgreSQL
```

Không sử dụng MySQL.

Không sử dụng MyBatis.

---

# 5. `users`

Schema logic:

```text
users
├── id
├── email
├── password_hash
├── status
├── created_at
└── updated_at
```

Domain representation:

```text
User
├── id
├── email
├── passwordHash
├── status
├── createdAt
└── updatedAt
```

Database field:

```text
password_hash
```

không bao giờ được dùng để lưu plaintext password.

---

# 6. `refresh_tokens`

Schema:

```text
refresh_tokens
├── id
├── user_id
├── token_hash
├── expires_at
├── revoked_at
└── created_at
```

Flow:

```text
Client
   │
   │ raw refresh token
   ▼
Hash token
   │
   ▼
refresh_tokens.token_hash
```

Không lưu raw refresh token trong PostgreSQL.

Khi client gửi refresh token:

```text
raw token
    ↓
hash(token)
    ↓
find token_hash
    ↓
validate
    ↓
check expiration
    ↓
check revoked_at
```

---

# 7. Database Relationship

Relationship:

```text
users
  │
  │ 1
  │
  │ N
  ▼
refresh_tokens
```

Database:

```text
users.id
   │
   └────────── refresh_tokens.user_id
```

`refresh_tokens.user_id` là foreign key tới `users.id`.

Codex phải kiểm tra migration hiện tại trước khi tạo migration mới.

Không tạo duplicate migration hoặc duplicate table.

---

# 8. Domain Layer

Domain layer phải độc lập với:

- PostgreSQL
- JPA
- Hibernate
- Spring Data JPA
- Flyway
- Controller
- HTTP
- SQL
- database annotations

Package đề xuất:

```text
domain
│
├── model
│   ├── User
│   ├── UserId
│   ├── Email
│   ├── UserStatus
│   ├── RefreshToken
│   └── RefreshTokenId
│
└── repository
    ├── UserRepository
    └── RefreshTokenRepository
```

---

# 9. `UserId`

`UserId` là Value Object.

Skeleton:

```java
public final class UserId {

    private final Long value;

    // TODO:
    // Implement constructor hoặc factory method.
    //
    // Yêu cầu:
    // - Không cho phép null.
    // - UserId phải immutable.
    // - Không có setter.
    // - Xác định validation phù hợp.

    // TODO:
    // Implement accessor.

    // TODO:
    // Implement equals().

    // TODO:
    // Implement hashCode().
}
```

Không sử dụng JPA annotation trong `UserId` ở bước đầu nếu chưa thực sự cần.

---

# 10. `RefreshTokenId`

Tương tự `UserId`.

```java
public final class RefreshTokenId {

    private final Long value;

    // TODO:
    // Implement constructor/factory.

    // TODO:
    // Implement accessor.

    // TODO:
    // Implement equals().

    // TODO:
    // Implement hashCode().
}
```

Mục tiêu là phân biệt:

```text
UserId
```

và:

```text
RefreshTokenId
```

thay vì sử dụng `Long` ở mọi nơi.

---

# 11. `Email`

Email có thể được thiết kế dưới dạng Value Object.

```java
public final class Email {

    private final String value;

    // TODO:
    // Implement constructor/factory.
    //
    // Cần quyết định:
    // - null xử lý thế nào?
    // - empty string xử lý thế nào?
    // - normalize email hay không?
    // - validation đặt ở đâu?

    // TODO:
    // Implement accessor.

    // TODO:
    // Implement equals().

    // TODO:
    // Implement hashCode().
}
```

Không đưa JPA annotation vào Domain chỉ vì Email được lưu trong database.

Nếu sau này cần JPA `@Embeddable`, phải cân nhắc rõ boundary giữa Domain Model và Persistence Model.

---

# 12. `UserStatus`

Tạo enum:

```java
public enum UserStatus {

    // TODO:
    // Xác định trạng thái cần thiết.
    //
    // Ví dụ:
    // ACTIVE
    // DISABLED
    // LOCKED
}
```

Không tự ý tạo quá nhiều trạng thái nếu requirement chưa cần.

---

# 13. Domain Entity — `User`

Domain Entity:

```text
User
├── UserId
├── Email
├── passwordHash
├── UserStatus
├── createdAt
└── updatedAt
```

Skeleton:

```java
public class User {

    private UserId id;

    private Email email;

    private String passwordHash;

    private UserStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    // TODO:
    // Thiết kế constructor hoặc factory method.

    // TODO:
    // Xác định domain invariants.

    // TODO:
    // Implement các behavior cần thiết.
    //
    // Ví dụ:
    // activate()
    // disable()
    // changeStatus(...)
}
```

Không thêm:

```java
@Entity
@Table
@Column
```

vào Domain Entity ở bước này.

Domain Entity không biết Hibernate tồn tại.

---

# 14. Domain Entity — `RefreshToken`

```text
RefreshToken
├── RefreshTokenId
├── UserId
├── tokenHash
├── expiresAt
├── revokedAt
└── createdAt
```

Skeleton:

```java
public class RefreshToken {

    private RefreshTokenId id;

    private UserId userId;

    private String tokenHash;

    private Instant expiresAt;

    private Instant revokedAt;

    private Instant createdAt;

    // TODO:
    // Implement constructor/factory.

    // TODO:
    // Implement isExpired().

    // TODO:
    // Implement isRevoked().

    // TODO:
    // Implement revoke().

    // TODO:
    // Xác định các domain invariant.
}
```

Không lưu raw refresh token.

---

# 15. Repository Abstraction

Repository interface nằm trong Domain:

```java
public interface UserRepository {

    // TODO:
    // Xác định method cần thiết.
    //
    // Ví dụ:
    // Optional<User> findByEmail(Email email);

    // TODO:
    // Xác định method save(User user).
}
```

Repository chỉ mô tả:

> Domain/Application cần database làm gì.

Repository không được biết:

```text
EntityManager
JpaRepository
Hibernate
PostgreSQL
JPQL
SQL
```

---

# 16. `RefreshTokenRepository`

```java
public interface RefreshTokenRepository {

    // TODO:
    // Xác định method tìm refresh token bằng token hash.

    // TODO:
    // Xác định method save.

    // TODO:
    // Xác định method revoke.

}
```

Không đưa persistence implementation vào Domain.

---

# 17. Infrastructure Layer

Persistence nằm trong Infrastructure.

Package đề xuất:

```text
infrastructure
│
└── persistence
    │
    ├── entity
    │   ├── UserJpaEntity
    │   └── RefreshTokenJpaEntity
    │
    ├── repository
    │   ├── UserJpaRepository
    │   ├── RefreshTokenJpaRepository
    │   ├── UserRepositoryImpl
    │   └── RefreshTokenRepositoryImpl
    │
    └── mapper
        ├── UserPersistenceMapper
        └── RefreshTokenPersistenceMapper
```

---

# 18. JPA Entity

Persistence Entity có trách nhiệm mapping database.

Ví dụ:

```java
@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private Long id;

    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // TODO:
    // Hoàn thiện constructor phù hợp với JPA.

    // TODO:
    // Implement getter/setter hoặc phương thức truy cập phù hợp.

    // TODO:
    // Xác định mapping constraints.
}
```

Lưu ý:

`UserJpaEntity` là Persistence Model.

Nó không phải Domain Entity `User`.

---

# 19. JPA Mapping

Persistence:

```text
User
   │
   │ mapping
   ▼
UserJpaEntity
   │
   ▼
Hibernate
   │
   ▼
PostgreSQL
```

Chiều ngược:

```text
PostgreSQL
   │
   ▼
Hibernate
   │
   ▼
UserJpaEntity
   │
   │ mapping
   ▼
User
```

Không để Domain Entity phụ thuộc Hibernate.

---

# 20. `RefreshTokenJpaEntity`

Skeleton:

```java
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity {

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    // TODO:
    // Hoàn thiện JPA mapping.

    // TODO:
    // Xác định constructor.

    // TODO:
    // Xác định accessor.

}
```

---

# 21. JPA Relationship

Database có:

```text
users
   │
   │ 1:N
   ▼
refresh_tokens
```

Có thể biểu diễn relationship bằng JPA.

Tuy nhiên:

> Không tự động thêm `@ManyToOne` hoặc `@OneToMany` chỉ vì database có foreign key.

Trước tiên hãy xem xét:

- Auth Service cần load User cùng RefreshToken không?
- Có cần navigation object không?
- Có gây lazy loading ngoài ý muốn không?
- Có tạo coupling không?
- Query nào thực sự cần relationship?

Nếu chưa cần, có thể giữ:

```java
private Long userId;
```

ở Persistence Entity.

Codex phải **ghi rõ lý do lựa chọn**.

---

# 22. Spring Data JPA Repository

Spring Data repository nằm trong Infrastructure.

Ví dụ:

```java
public interface UserJpaRepository
        extends JpaRepository<UserJpaEntity, Long> {

    // TODO:
    // Define persistence query cần thiết.
    //
    // Ví dụ:
    // tìm UserJpaEntity bằng email.
}
```

Điểm quan trọng:

```text
UserRepository
```

và:

```text
UserJpaRepository
```

là **hai abstraction khác nhau**.

---

# 23. Repository Implementation

Infrastructure implement Domain Repository:

```java
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository repository;

    // TODO:
    // Constructor injection.

    @Override
    public Optional<User> findByEmail(Email email) {

        // TODO:
        // 1. Convert Email -> String.
        // 2. Gọi UserJpaRepository.
        // 3. Map UserJpaEntity -> User.
        return null;
    }

    @Override
    public User save(User user) {

        // TODO:
        // 1. Map User -> UserJpaEntity.
        // 2. Save bằng Spring Data JPA.
        // 3. Map Entity -> Domain.
        return null;
    }
}
```

Không đưa `JpaRepository` vào Domain.

---

# 24. Persistence Mapper

Tạo mapper riêng:

```java
public final class UserPersistenceMapper {

    // TODO:
    // Implement:
    //
    // User -> UserJpaEntity

    // TODO:
    // Implement:
    //
    // UserJpaEntity -> User
}
```

Mapping:

```text
UserId
   ↕
Long
```

```text
Email
   ↕
String
```

```text
UserStatus
   ↕
String hoặc enum
```

Developer phải quyết định persistence representation phù hợp.

---

# 25. Enum Persistence

Đối với:

```text
UserStatus
```

Codex không được tự ý dùng ordinal.

Không dùng:

```java
@Enumerated(EnumType.ORDINAL)
```

trừ khi có lý do rất rõ ràng.

Ưu tiên xem xét:

```java
@Enumerated(EnumType.STRING)
```

hoặc persistence mapping riêng.

Guide phải giải thích:

> Tại sao không nên lưu enum bằng ordinal?

---

# 26. PostgreSQL

Application phải sử dụng PostgreSQL datasource.

Kiểm tra:

```text
application.yml
application-local.yml
application-dev.yml
```

hoặc cấu hình tương ứng hiện tại.

Không hard-code:

```text
username
password
DATABASE_URL
```

vào source code.

Sử dụng environment variables hoặc configuration phù hợp.

---

# 27. Flyway

Flyway chịu trách nhiệm database schema.

Không dùng Hibernate để tự động tạo schema production.

Không dùng:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
```

hoặc:

```text
create-drop
```

cho môi trường production.

Ưu tiên:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Hibernate phải validate schema do Flyway tạo.

Flow:

```text
Flyway
   ↓
PostgreSQL Schema
   ↓
Hibernate validate
```

Không:

```text
Hibernate
   ↓
Tự tạo database schema
```

---

# 28. Architecture

Architecture mục tiêu:

```text
┌──────────────────────────────┐
│         Controller           │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│      Application Layer       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         Domain Layer         │
│                              │
│ User                         │
│ UserId                       │
│ Email                        │
│ UserStatus                   │
│ RefreshToken                 │
│ Repository Interfaces        │
└──────────────┬───────────────┘
               │
               │ implemented by
               ▼
┌──────────────────────────────┐
│      Infrastructure          │
│                              │
│ RepositoryImpl               │
│ Spring Data JPA              │
│ JPA Entity                   │
│ Persistence Mapper           │
│ Hibernate                    │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         PostgreSQL           │
└──────────────────────────────┘
```

---

# 29. Dependency Direction

Nguyên tắc:

```text
Domain
  ↑
  │
Infrastructure
```

Infrastructure phụ thuộc Domain.

Domain không phụ thuộc Infrastructure.

Domain không import:

```text
org.springframework
jakarta.persistence
org.hibernate
org.postgresql
```

Nếu thấy:

```java
import jakarta.persistence.Entity;
```

trong Domain Entity:

> Dừng và kiểm tra lại architecture.

JPA annotation thuộc Persistence Model.

---

# 30. Thứ tự Codex thực hiện

## Step 1 — Inspect project

Kiểm tra:

```text
pom.xml
src/main/java
src/main/resources
Flyway migrations
application configuration
PostgreSQL configuration
JPA configuration
```

Không tự ý refactor architecture hiện tại.

---

## Step 2 — Verify PostgreSQL schema

Xác nhận:

```text
auth_db
├── users
└── refresh_tokens
```

Kiểm tra:

- primary key
- foreign key
- unique constraint
- nullable
- timestamp
- indexes
- password_hash
- token_hash

Nếu schema không khớp:

```text
DO NOT silently modify it.
```

Ghi lại mismatch.

---

## Step 3 — Create Domain Model

Tạo:

```text
User
UserId
Email
UserStatus
RefreshToken
RefreshTokenId
```

Chỉ skeleton + TODO.

---

## Step 4 — Create Domain Repository

Tạo:

```text
UserRepository
RefreshTokenRepository
```

Chỉ abstraction.

---

## Step 5 — Create JPA Persistence Model

Tạo:

```text
UserJpaEntity
RefreshTokenJpaEntity
```

Chỉ persistence concerns.

---

## Step 6 — Create Spring Data Repository

Tạo:

```text
UserJpaRepository
RefreshTokenJpaRepository
```

---

## Step 7 — Create Persistence Mapper

Tạo:

```text
UserPersistenceMapper
RefreshTokenPersistenceMapper
```

---

## Step 8 — Create Repository Implementation

Tạo:

```text
UserRepositoryImpl
RefreshTokenRepositoryImpl
```

Flow:

```text
Domain Repository
       ↓
RepositoryImpl
       ↓
Spring Data JPA Repository
       ↓
Hibernate
       ↓
PostgreSQL
```

---

# 31. Không làm trong phase này

Không triển khai full:

```text
Login API
Signup API
JWT
Access Token
Refresh Token Rotation
Password Hashing Service
Spring Security
Authentication Filter
Authorization
RBAC
Gateway
User Service
```

Phase này chỉ tập trung:

```text
PostgreSQL
    ↓
Domain
    ↓
Repository abstraction
    ↓
JPA Persistence
    ↓
Hibernate
    ↓
PostgreSQL
```

---

# 32. Learning Guide

Sau khi hoàn thành skeleton code, Codex **BẮT BUỘC** tạo:

```text
docs/AUTH-DOMAIN-LEARNING-GUIDE.md
```

File này dành cho developer đọc và tự hoàn thiện code.

Guide phải giải thích:

## 32.1 Domain Layer

- Entity là gì?
- Value Object là gì?
- `User` tại sao là Entity?
- `UserId` tại sao là Value Object?
- `Email` tại sao có thể là Value Object?

---

## 32.2 Repository Pattern

Giải thích:

```text
UserRepository
```

và:

```text
UserRepositoryImpl
```

khác nhau như thế nào.

Giải thích Dependency Inversion.

---

## 32.3 JPA Entity

Giải thích:

```text
User
```

khác:

```text
UserJpaEntity
```

như thế nào.

Đặc biệt giải thích:

> Tại sao không đơn giản thêm `@Entity` vào Domain Entity?

---

## 32.4 Hibernate

Giải thích flow:

```text
UserJpaRepository
        ↓
Hibernate
        ↓
SQL
        ↓
PostgreSQL
```

Giải thích Hibernate thực hiện gì.

---

## 32.5 Spring Data JPA

Giải thích:

```text
JpaRepository
```

cung cấp gì.

Giải thích:

```text
UserJpaRepository
```

khác:

```text
UserRepository
```

như thế nào.

---

## 32.6 Persistence Mapping

Giải thích:

```text
Domain
   ↓
Persistence Mapper
   ↓
JPA Entity
   ↓
Hibernate
   ↓
PostgreSQL
```

và chiều ngược lại.

---

## 32.7 Flyway vs Hibernate

Giải thích:

```text
Flyway
    ↓
Database Schema
```

và:

```text
Hibernate
    ↓
Validate / ORM
```

Tại sao không để Hibernate tự tạo production schema.

---

# 33. TODO Checklist

Trong `AUTH-DOMAIN-LEARNING-GUIDE.md` phải có checklist:

```text
[ ] Implement UserId
[ ] Implement RefreshTokenId
[ ] Implement Email
[ ] Define UserStatus

[ ] Implement User
[ ] Implement RefreshToken

[ ] Define UserRepository
[ ] Define RefreshTokenRepository

[ ] Implement UserJpaEntity
[ ] Implement RefreshTokenJpaEntity

[ ] Implement UserJpaRepository
[ ] Implement RefreshTokenJpaRepository

[ ] Implement UserPersistenceMapper
[ ] Implement RefreshTokenPersistenceMapper

[ ] Implement UserRepositoryImpl
[ ] Implement RefreshTokenRepositoryImpl

[ ] Configure PostgreSQL
[ ] Verify Flyway
[ ] Set ddl-auto=validate

[ ] Run application
[ ] Verify Hibernate startup
[ ] Verify database connection

[ ] Write repository tests
[ ] Verify domain does not depend on JPA
```

---

# 34. Coding Order

Developer phải được hướng dẫn code theo thứ tự:

```text
Step 1
Value Objects
    ↓
Step 2
Domain Entities
    ↓
Step 3
Domain Repository Interfaces
    ↓
Step 4
JPA Entities
    ↓
Step 5
Spring Data JPA Repository
    ↓
Step 6
Persistence Mapping
    ↓
Step 7
Repository Implementation
    ↓
Step 8
Integration Test
```

Không code tất cả cùng lúc.

---

# 35. TODO phải có "Why"

Ví dụ:

```java
// TODO:
// Why use UserId instead of Long?
//
// Think about:
// - Type safety
// - Domain meaning
// - Prevent mixing IDs
// - Future validation
```

Repository:

```java
// TODO:
// Why is this interface inside Domain?
//
// Think about:
// - Dependency Inversion
// - Domain defines what it needs
// - Infrastructure defines how it accesses PostgreSQL
```

JPA Entity:

```java
// TODO:
// Why is UserJpaEntity different from User?
//
// Think about:
// - Persistence concerns
// - ORM annotations
// - Coupling
// - Domain purity
```

---

# 36. Không tạo fake implementation

Không làm:

```java
return null;
```

mà không có TODO.

Nếu cần placeholder:

```java
// TODO:
// Implement this method.
// Read AUTH-DOMAIN-LEARNING-GUIDE.md
throw new UnsupportedOperationException("TODO");
```

Mục tiêu:

```text
Codex tạo structure
       ↓
Developer đọc TODO
       ↓
Developer đọc Learning Guide
       ↓
Developer tự implement
       ↓
Developer test
```

---

# 37. Code Quality

Codex phải:

- dùng constructor injection
- không field injection
- không thêm dependency không cần thiết
- không refactor ngoài scope
- không xóa code hiện tại
- không duplicate migration
- không duplicate table
- không commit secret
- không log password
- không log raw refresh token
- không để Domain phụ thuộc JPA
- không để Domain phụ thuộc Spring
- giữ package naming nhất quán

---

# 38. Testing

Sau khi tạo skeleton:

```text
mvn test
```

hoặc command tương ứng với project.

Kiểm tra:

```text
Domain
  X jakarta.persistence
  X org.hibernate
  X org.springframework
```

Infrastructure:

```text
Infrastructure
  ✓ Domain
  ✓ Spring Data JPA
  ✓ Hibernate
  ✓ PostgreSQL
```

Nếu project có ArchUnit:

```text
Domain must not depend on Infrastructure.
```

---

# 39. Final Report

Codex phải báo cáo:

```text
1. Files created
2. Files modified
3. PostgreSQL schema verified
4. Domain models created
5. Repository abstractions created
6. JPA entities created
7. Spring Data repositories created
8. Persistence mappers created
9. Repository implementations created
10. TODO areas intentionally left
11. Tests/checks executed
12. Remaining work
```

Đặc biệt phải tách:

```text
WHAT CODEX IMPLEMENTED
```

và:

```text
WHAT DEVELOPER MUST IMPLEMENT
```

---

# 40. Definition of Done

```text
[✓] PostgreSQL schema verified
[✓] Domain package created
[✓] Value Object skeleton
[✓] Domain Entity skeleton
[✓] Repository interfaces
[✓] JPA Entity skeleton
[✓] Spring Data repository skeleton
[✓] Persistence Mapper skeleton
[✓] Repository implementation skeleton
[✓] Dependency direction correct
[✓] Learning Guide created
[✓] TODO contains explanation
[✓] Build/compile checked
```

Không yêu cầu hoàn thành:

```text
[ ] Login
[ ] Signup
[ ] JWT
[ ] Spring Security
[ ] RBAC
[ ] Gateway
```

Đây là **learning-oriented Auth Domain & Persistence phase**, không phải production-complete authentication phase.
