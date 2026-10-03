# Auth Domain & Persistence Learning Guide

Đây là skeleton học tập, chưa phải authentication implementation hoàn chỉnh. Các phương thức có `TODO` cố ý ném `UnsupportedOperationException`; hãy hoàn thiện theo từng bước và viết test trước khi nối application flow.

## Kiến trúc

`auth-domain` chỉ chứa model và repository abstraction thuần Java. Nó không import Spring, JPA hay Hibernate. `auth-infrastructure` triển khai các abstraction bằng JPA/Hibernate. `auth-start` là composition root và cấu hình PostgreSQL/Flyway. Repository domain trả lời “cần làm gì”; adapter infrastructure trả lời “làm bằng cách nào”.

## Domain model

`User` và `RefreshToken` là entity vì chúng có identity và vòng đời. `UserId`, `RefreshTokenId`, và `Email` là value object vì chúng biểu diễn giá trị có ý nghĩa domain và nên immutable. Hãy hoàn thiện constructor, validation, accessor, `equals`, `hashCode` trong các file model. `UserStatus` chỉ nên giữ các trạng thái thực sự cần.

`passwordHash` và `tokenHash` là dữ liệu đã băm. Không thêm raw password hoặc raw refresh token vào entity, log hay database. Với `RefreshToken`, hoàn thiện `isExpired`, `isRevoked`, `revoke` và quyết định invariant về thời gian bằng `Clock` để test ổn định.

## Repository pattern

`UserRepository`/`RefreshTokenRepository` thuộc domain và không biết `JpaRepository`, SQL hay EntityManager. `UserRepositoryImpl`/`RefreshTokenRepositoryImpl` thuộc infrastructure và chuyển đổi qua mapper. Đây là Dependency Inversion: domain định nghĩa nhu cầu, infrastructure cung cấp chi tiết.

## JPA và Hibernate

`User` khác `UserJpaEntity`: domain giữ luật nghiệp vụ; JPA entity giữ annotation và mapping bảng. Không gắn `@Entity` vào domain vì như vậy domain bị coupling với ORM và schema. `UserJpaRepository` mở rộng Spring Data `JpaRepository`; Spring Data tạo query, Hibernate chuyển thao tác thành SQL rồi gửi tới PostgreSQL.

Mapper phải chuyển `UserId ↔ Long`, `Email ↔ String`, `UserStatus ↔ String/enum` một cách rõ ràng. Không dùng enum ordinal vì thay đổi thứ tự enum có thể làm dữ liệu cũ mang nghĩa khác; ưu tiên tên ổn định hoặc mapping riêng.

Giữ `userId` dạng scalar trong `RefreshTokenJpaEntity` ở bước đầu. Chỉ dùng `@ManyToOne`/`@OneToMany` khi use case thật sự cần navigation và bạn đã đánh giá lazy loading, query và coupling.

## Flyway và PostgreSQL

Repository hiện chưa có thư mục migration, vì vậy schema `auth_db`, `users`, `refresh_tokens`, khóa ngoại, unique constraint và index chưa được xác nhận. Hãy kiểm tra database/migration bên ngoài repository trước khi tạo `V__` migration; không tạo duplicate table.

Flyway tạo và version schema. Hibernate chỉ ORM và `ddl-auto: validate` để kiểm tra schema. Không dùng `create` hoặc `create-drop` cho production. Cấu hình dùng `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`; không commit secret.

## Coding order

1. Hoàn thiện `UserId`, `RefreshTokenId`, `Email`.
2. Hoàn thiện `User`, `RefreshToken`, status transitions và invariants.
3. Chốt method repository interfaces.
4. Hoàn thiện JPA constructors/accessors/constraints.
5. Xác nhận migration rồi hoàn thiện Spring Data queries.
6. Hoàn thiện hai mapper.
7. Hoàn thiện repository adapters bằng constructor injection.
8. Viết unit test domain và integration test PostgreSQL/Flyway.

## Checklist

- [ ] Implement UserId
- [ ] Implement RefreshTokenId
- [ ] Implement Email
- [ ] Define UserStatus
- [ ] Implement User
- [ ] Implement RefreshToken
- [ ] Define UserRepository
- [ ] Define RefreshTokenRepository
- [ ] Implement UserJpaEntity
- [ ] Implement RefreshTokenJpaEntity
- [ ] Implement UserJpaRepository
- [ ] Implement RefreshTokenJpaRepository
- [ ] Implement UserPersistenceMapper
- [ ] Implement RefreshTokenPersistenceMapper
- [ ] Implement UserRepositoryImpl
- [ ] Implement RefreshTokenRepositoryImpl
- [ ] Configure PostgreSQL
- [ ] Verify Flyway migrations and schema
- [ ] Keep `ddl-auto=validate`
- [ ] Run application and verify connection
- [ ] Write repository tests
- [ ] Verify domain has no JPA/Spring imports
