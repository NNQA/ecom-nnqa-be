package ecom.mta.authinfrastructure;

import ecom.mta.authinfrastructure.persistence.entity.UserJpaEntity;
import ecom.mta.authinfrastructure.persistence.mapper.UserPersistenceMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserPersistenceIdentityTest {
    @Test
    void rejectsPersistedUserWithoutIdentity() {
        Instant now = Instant.now();
        UserJpaEntity entity = new UserJpaEntity(
                null, "user@example.com", "opaque-hash", "PENDING", now, now);

        assertThrows(IllegalStateException.class, () -> UserPersistenceMapper.toDomain(entity));
    }
}
