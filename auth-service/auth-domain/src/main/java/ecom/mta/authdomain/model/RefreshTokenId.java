package ecom.mta.authdomain.model;

import java.util.Objects;

public record RefreshTokenId(Long value) {
    public RefreshTokenId {
        Objects.requireNonNull(value, "Refresh token ID cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("Refresh token ID must be greater than zero");
        }
    }

    public static RefreshTokenId of(Long value) {
        return new RefreshTokenId(value);
    }
}
