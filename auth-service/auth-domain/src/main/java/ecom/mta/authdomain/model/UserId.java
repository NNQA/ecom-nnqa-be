package ecom.mta.authdomain.model;

import java.util.Objects;

public record UserId (Long value) {
    public UserId {
        Objects.requireNonNull(value, "User ID cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("User ID must be greater than 0");
        }
    }
    public static UserId of(Long value) {
        return new UserId(value);
    }
}
