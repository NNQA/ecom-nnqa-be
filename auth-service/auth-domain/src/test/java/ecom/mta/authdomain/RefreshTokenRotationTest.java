package ecom.mta.authdomain;
import ecom.mta.authdomain.model.*; import org.junit.jupiter.api.Test; import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
class RefreshTokenRotationTest {
 @Test void requiresPositiveTtlAndSupportsIdempotentRotation() { Instant t=Instant.parse("2026-01-01T00:00:00Z"); assertThrows(IllegalArgumentException.class,()->RefreshToken.createNew(RefreshTokenId.of(1L),UserId.of(1L),"hash",t,0)); var token=RefreshToken.createNew(RefreshTokenId.of(1L),UserId.of(1L),"hash",t,60); token.rotateTo(RefreshTokenId.of(2L),t.plusSeconds(1)); token.rotateTo(RefreshTokenId.of(2L),t.plusSeconds(2)); assertTrue(token.isRevoked()); assertEquals(RefreshTokenId.of(2L),token.getReplacedByTokenId()); }
}
