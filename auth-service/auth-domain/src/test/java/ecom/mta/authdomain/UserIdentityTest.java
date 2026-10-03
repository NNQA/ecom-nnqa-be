package ecom.mta.authdomain;

import ecom.mta.authdomain.model.Email;
import ecom.mta.authdomain.model.User;
import ecom.mta.authdomain.model.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNull;

class UserIdentityTest {
    @Test
    void newUserMayHaveNoDatabaseIdentityYet() {
        User user = new User(null, new Email("user@example.com"), "opaque-hash",
                UserStatus.PENDING, Instant.now(), Instant.now());

        assertNull(user.getId());
    }
}
