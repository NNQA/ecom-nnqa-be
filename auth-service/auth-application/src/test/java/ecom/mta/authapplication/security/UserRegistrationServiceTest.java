package ecom.mta.authapplication.security;

import ecom.mta.authdomain.model.Email;
import ecom.mta.authdomain.model.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationServiceTest {
    @Test
    void hashesRawPasswordBeforeCreatingUser() {
        PasswordHasher hasher = new PasswordHasher() {
            @Override public String hash(String rawPassword) { assertEquals("raw-password", rawPassword); return "opaque-hash"; }
            @Override public boolean matches(String rawPassword, String passwordHash) { return false; }
        };
        var user = new UserRegistrationService(hasher, new PasswordPolicy())
                .register(null, new Email("user@example.com"), "raw-password");
        assertEquals("opaque-hash", user.getPasswordHash());
        assertEquals(UserStatus.PENDING, user.getStatus());
    }

    @Test
    void rejectsPasswordBeforeHasherIsCalled() {
        PasswordHasher hasher = new PasswordHasher() {
            @Override public String hash(String rawPassword) { fail("hasher must not be called"); return ""; }
            @Override public boolean matches(String rawPassword, String passwordHash) { return false; }
        };
        assertThrows(IllegalArgumentException.class, () ->
                new UserRegistrationService(hasher, new PasswordPolicy())
                        .register(null, new Email("user@example.com"), "short"));
    }
}
