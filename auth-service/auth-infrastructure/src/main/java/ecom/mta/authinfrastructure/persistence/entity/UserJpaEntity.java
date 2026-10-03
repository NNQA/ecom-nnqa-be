package ecom.mta.authinfrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(name = "email_verified_at") private Instant emailVerifiedAt;
    @Column(name = "failed_login_attempts", nullable = false) private int failedLoginAttempts;
    @Column(name = "locked_until") private Instant lockedUntil;
    @Column(name = "password_changed_at") private Instant passwordChangedAt;
    @Column(name = "last_login_at") private Instant lastLoginAt;

    protected UserJpaEntity() { }

    public UserJpaEntity(Long id, String email, String passwordHash,
                         String status, Instant createdAt, Instant updatedAt) {
        this(id, email, passwordHash, status, createdAt, updatedAt, null, 0, null, null, null);
    }
    public UserJpaEntity(Long id, String email, String passwordHash, String status,
                         Instant createdAt, Instant updatedAt, Instant emailVerifiedAt,
                         int failedLoginAttempts, Instant lockedUntil, Instant passwordChangedAt,
                         Instant lastLoginAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.emailVerifiedAt = emailVerifiedAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
        this.passwordChangedAt = passwordChangedAt;
        this.lastLoginAt = lastLoginAt;
    }


    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getEmailVerifiedAt() { return emailVerifiedAt; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public Instant getLockedUntil() { return lockedUntil; }
    public Instant getPasswordChangedAt() { return passwordChangedAt; }
    public Instant getLastLoginAt() { return lastLoginAt; }
}
