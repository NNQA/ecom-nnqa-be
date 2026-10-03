package ecom.mta.authdomain.model;

import java.time.Instant;
import java.util.Objects;

public class RefreshToken {
    private final RefreshTokenId id;
    private final UserId userId;
    private final String tokenHash;
    private final Instant expiresAt;
    private Instant revokedAt; // Mutable: can transition to a revoked state
    private final Instant createdAt;
    private RefreshTokenId replacedByTokenId;


    public RefreshToken(RefreshTokenId id, UserId userId, String tokenHash,
                        Instant expiresAt, Instant revokedAt, Instant createdAt) {
        this(id, userId, tokenHash, expiresAt, revokedAt, createdAt, null);
    }
    public RefreshToken(RefreshTokenId id, UserId userId, String tokenHash,
                        Instant expiresAt, Instant revokedAt, Instant createdAt,
                        RefreshTokenId replacedByTokenId) {

        Objects.requireNonNull(id, "Refresh token ID is mandatory.");
        Objects.requireNonNull(userId, "Associated User ID is mandatory.");

        if (tokenHash == null || tokenHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Token hash is mandatory.");
        }
        Objects.requireNonNull(expiresAt, "Expiration timestamp is mandatory.");
        Objects.requireNonNull(createdAt, "Creation timestamp is mandatory.");

        if (expiresAt.isBefore(createdAt) || expiresAt.equals(createdAt)) {
            throw new IllegalArgumentException("Expiration time must be strictly after creation time.");
        }

        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
        this.replacedByTokenId = replacedByTokenId;
    }

    public static RefreshToken createNew(RefreshTokenId id, UserId userId, String tokenHash, Instant createdAt, long ttlSeconds) {
        if (ttlSeconds <= 0) throw new IllegalArgumentException("Refresh token TTL must be positive.");
        Instant expiresAt = createdAt.plusSeconds(ttlSeconds);
        return new RefreshToken(id, userId, tokenHash, expiresAt, null, createdAt);
    }


    public boolean isExpired(Instant now) {
        Objects.requireNonNull(now, "Reference time cannot be null for expiration check.");
        return now.isAfter(this.expiresAt) || now.equals(this.expiresAt);
    }

    public boolean isRevoked() {
        return this.revokedAt != null;
    }

    public void revoke(Instant revokedAt) {
        Objects.requireNonNull(revokedAt, "Revocation timestamp is mandatory.");

        if (revokedAt.isBefore(this.createdAt)) {
            throw new IllegalArgumentException("Revocation time cannot occur before token creation.");
        }

        if (isRevoked()) {
            return;
        }

        this.revokedAt = revokedAt;
    }

    public void rotateTo(RefreshTokenId replacementId, Instant revokedAt) {
        Objects.requireNonNull(replacementId, "Replacement token ID is mandatory.");
        revoke(revokedAt);
        this.replacedByTokenId = replacementId;
    }


    public RefreshTokenId getId() { return id; }
    public UserId getUserId() { return userId; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public RefreshTokenId getReplacedByTokenId() { return replacedByTokenId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
