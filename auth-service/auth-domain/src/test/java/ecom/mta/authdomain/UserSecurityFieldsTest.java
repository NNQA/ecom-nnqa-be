package ecom.mta.authdomain;

import ecom.mta.authdomain.model.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserSecurityFieldsTest {
    @Test
    void tracksFailedLoginsAndLockWindow() {
        Instant now = Instant.parse("2026-01-01T00:00:00Z");
        User u = new User(UserId.of(1L), new Email("A@EXAMPLE.COM"), "$2a$10$abcdefghijklmnopqrstuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu", UserStatus.ACTIVE, now, now, now, 2, null, now, now);
        u.recordFailedLogin();
        assertEquals(3, u.getFailedLoginAttempts());
        u.lockUntil(now.plusSeconds(60));
        assertTrue(u.isLockedAt(now));
        u.resetFailedLoginAttempts();
        assertEquals(0, u.getFailedLoginAttempts());
    }
}
