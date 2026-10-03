package ecom.mta.authinfrastructure.security;
import ecom.mta.authapplication.security.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class BCryptPasswordHasher implements PasswordHasher {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    public String hash(String rawPassword) {
        if(rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }

        return encoder.encode(rawPassword);
    }
    public boolean matches(String rawPassword, String passwordHash) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            return false;
        }
        return encoder.matches(rawPassword, passwordHash);
    }
}
