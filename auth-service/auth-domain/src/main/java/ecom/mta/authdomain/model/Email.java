package ecom.mta.authdomain.model;

import java.util.Objects;
import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern BASELINE_FORMAT =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        if (!BASELINE_FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException("Email format is invalid");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Email email = (Email) other;
        return Objects.equals(value, email.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
