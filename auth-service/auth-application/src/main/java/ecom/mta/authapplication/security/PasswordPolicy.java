package ecom.mta.authapplication.security;

import ecom.mta.authdomain.model.Email;

public class PasswordPolicy {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    public void validate(String rawPassword, Email email) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (rawPassword.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters."
            );
        }

        if (rawPassword.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must not exceed 128 characters."
            );
        }

        if (email != null &&
                rawPassword.equalsIgnoreCase(email.value())) {
            throw new IllegalArgumentException(
                    "Password cannot be the same as email."
            );
        }
    }
}
