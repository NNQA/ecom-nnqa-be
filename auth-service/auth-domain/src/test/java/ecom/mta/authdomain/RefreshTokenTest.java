package ecom.mta.authdomain;

import ecom.mta.authdomain.model.RefreshToken;
import ecom.mta.authdomain.model.RefreshTokenId;
import ecom.mta.authdomain.model.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {
    private static final Instant CREATED = Instant.parse("2026-01-01T00:00:00Z");
    private static final Instant EXPIRES = CREATED.plusSeconds(3600);

    @Test void shouldReportExpirationAtAndAfterExpiry() {
        var token = new RefreshToken(new RefreshTokenId(1L), UserId.of(2L), "hash", EXPIRES, null, CREATED);
        assertThat(token.isExpired(EXPIRES.minusNanos(1))).isFalse();
        assertThat(token.isExpired(EXPIRES)).isTrue();
        assertThat(token.isExpired(EXPIRES.plusSeconds(1))).isTrue();
    }

    @Test void shouldTransitionToRevokedOnlyOnce() {
        var token = new RefreshToken(new RefreshTokenId(1L), UserId.of(2L), "hash", EXPIRES, null, CREATED);
        assertThat(token.isRevoked()).isFalse();
        token.revoke(EXPIRES.minusSeconds(1));
        assertThat(token.isRevoked()).isTrue();
        token.revoke(EXPIRES);
        assertThat(token.isRevoked()).isTrue();
    }
}
