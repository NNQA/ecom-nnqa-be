package ecom.mta.authinfrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "replaced_by_token_id") private Long replacedByTokenId;

    protected RefreshTokenJpaEntity() { }

    public RefreshTokenJpaEntity(Long id, Long userId, String tokenHash,
                                 Instant expiresAt, Instant revokedAt, Instant createdAt) {
        this(id, userId, tokenHash, expiresAt, revokedAt, createdAt, null);
    }
    public RefreshTokenJpaEntity(Long id, Long userId, String tokenHash,
                                 Instant expiresAt, Instant revokedAt, Instant createdAt, Long replacedByTokenId) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
        this.replacedByTokenId = replacedByTokenId;
    }

    public void setReplacedByTokenId(Long value) { this.replacedByTokenId = value; }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getReplacedByTokenId() { return replacedByTokenId; }
}
