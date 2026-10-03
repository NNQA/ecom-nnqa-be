package ecom.mta.authdomain.repository;

import ecom.mta.authdomain.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    RefreshToken save(RefreshToken token);
}
