package ecom.mta.authinfrastructure;

import ecom.mta.authinfrastructure.security.BCryptPasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {
    private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    void hashesAndMatchesWithoutExposingRawPassword() {
        String hash = hasher.hash("correct horse battery staple");
        assertNotEquals("correct horse battery staple", hash);
        assertTrue(hasher.matches("correct horse battery staple", hash));
        assertFalse(hasher.matches("wrong password", hash));
    }

    @Test
    void rejectsBlankInputs() {
        assertThrows(IllegalArgumentException.class, () -> hasher.hash(" "));
        assertFalse(hasher.matches(" ", "hash"));
        assertFalse(hasher.matches("password", " "));
    }
}
