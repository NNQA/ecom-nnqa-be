package ecom.mta.authapplication.security;
import ecom.mta.authdomain.model.*;
public class UserRegistrationService {
    private final PasswordHasher passwordHasher;
    private final PasswordPolicy passwordPolicy;

    public UserRegistrationService(PasswordHasher passwordHasher, PasswordPolicy passwordPolicy) {
        this.passwordHasher = passwordHasher;
        this.passwordPolicy = passwordPolicy;
    }
    public User register(UserId id, Email email, String rawPassword) {
        passwordPolicy.validate(rawPassword, email);
        String passwordHash = passwordHasher.hash(rawPassword);
        return User.createNew(id, email, passwordHash);
    }
}
