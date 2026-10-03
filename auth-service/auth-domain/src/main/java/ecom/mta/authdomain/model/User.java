package ecom.mta.authdomain.model;

import java.time.Instant;
import java.util.Objects;

public class User {
    private final UserId id;
    private final Email email;
    private final String passwordHash;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final Instant emailVerifiedAt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private Instant passwordChangedAt;
    private Instant lastLoginAt;

    public User(UserId id, Email email, String passwordHash, UserStatus status,
                Instant createdAt, Instant updatedAt) {
        this(id, email, passwordHash, status, createdAt, updatedAt, null, 0, null, null, null);
    }

    public User(UserId id, Email email, String passwordHash, UserStatus status,
                Instant createdAt, Instant updatedAt, Instant emailVerifiedAt,
                int failedLoginAttempts, Instant lockedUntil, Instant passwordChangedAt,
                Instant lastLoginAt) {
        if (email == null) {
            throw new IllegalArgumentException("Email is mandatory.");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash is mandatory.");
        }
        if (status == null) {
            throw new IllegalArgumentException("User status cannot be null.");
        }
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        if (failedLoginAttempts < 0) throw new IllegalArgumentException("Failed login attempts cannot be negative.");
        this.emailVerifiedAt = emailVerifiedAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
        this.passwordChangedAt = passwordChangedAt;
        this.lastLoginAt = lastLoginAt;
    }

    public static User createNew(UserId id, Email email, String passwordHash) {
        Instant now = Instant.now();
        return new User(id, email, passwordHash, UserStatus.PENDING, now, now);
    }

    public void activate() {
        if (this.status == UserStatus.LOCKED) {
            throw new IllegalStateException("Invalid state transition: A locked account cannot be activated directly.");
        }
        changeStatus(UserStatus.ACTIVE);
    }

    public void disable() {
        changeStatus(UserStatus.DISABLED);
    }

    public void lock() {
        changeStatus(UserStatus.LOCKED);
    }

    public void changeStatus(UserStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("User status cannot be null.");
        }

        if (this.status == newStatus) {
            return;
        }

        if (this.status == UserStatus.LOCKED && newStatus == UserStatus.DISABLED) {
            throw new IllegalStateException("Invalid state transition: Cannot transition from LOCKED to DISABLED.");
        }

        this.status = newStatus;
        this.updatedAt = Instant.now();
    }


    public UserId getId() { return id; }
    public Email getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getEmailVerifiedAt() { return emailVerifiedAt; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public Instant getLockedUntil() { return lockedUntil; }
    public Instant getPasswordChangedAt() { return passwordChangedAt; }
    public Instant getLastLoginAt() { return lastLoginAt; }
    public void recordFailedLogin() { failedLoginAttempts++; }
    public void resetFailedLoginAttempts() { failedLoginAttempts = 0; }
    public void lockUntil(Instant until) { lockedUntil = Objects.requireNonNull(until); lock(); }
    public boolean isLockedAt(Instant now) { return status == UserStatus.LOCKED && (lockedUntil == null || lockedUntil.isAfter(now)); }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
