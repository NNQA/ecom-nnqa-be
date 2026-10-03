package ecom.mta.authinfrastructure;

import ecom.mta.authdomain.model.*;
import ecom.mta.authinfrastructure.persistence.mapper.UserPersistenceMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {
    @Test
    void roundTripsV7SecurityFields() {
        Instant t = Instant.parse("2026-01-01T00:00:00Z");
        User s = new User(UserId.of(7L), new Email("user@example.com"), "$2a$10$abcdefghijklmnopqrstuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu", UserStatus.LOCKED, t, t, t, 4, t.plusSeconds(300), t, t);
        User r = UserPersistenceMapper.toDomain(UserPersistenceMapper.toEntity(s));
        assertEquals(s.getEmailVerifiedAt(), r.getEmailVerifiedAt());
        assertEquals(s.getFailedLoginAttempts(), r.getFailedLoginAttempts());
        assertEquals(s.getLockedUntil(), r.getLockedUntil());
        assertEquals(s.getPasswordChangedAt(), r.getPasswordChangedAt());
        assertEquals(s.getLastLoginAt(), r.getLastLoginAt());
    }
}
