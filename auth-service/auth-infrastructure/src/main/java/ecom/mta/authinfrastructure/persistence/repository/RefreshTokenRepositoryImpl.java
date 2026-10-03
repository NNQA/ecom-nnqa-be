package ecom.mta.authinfrastructure.persistence.repository;

import ecom.mta.authdomain.model.RefreshToken;
import ecom.mta.authdomain.repository.RefreshTokenRepository; // Assuming this package structure for your domain port
import ecom.mta.authinfrastructure.persistence.entity.RefreshTokenJpaEntity;
import ecom.mta.authinfrastructure.persistence.mapper.RefreshTokenPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository repository;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        if (tokenHash == null || tokenHash.trim().isEmpty()) {
            return Optional.empty();
        }

        return repository.findByTokenHash(tokenHash)
                .map(RefreshTokenPersistenceMapper::toDomain);
    }


    @Override
    @Transactional
    public RefreshToken save(RefreshToken token) {
        if (token == null) {
            throw new IllegalArgumentException("Cannot save a null RefreshToken aggregate.");
        }

        RefreshTokenJpaEntity entityToSave = RefreshTokenPersistenceMapper.toEntity(token);

        RefreshTokenJpaEntity savedEntity = repository.save(entityToSave);

        return RefreshTokenPersistenceMapper.toDomain(savedEntity);
    }
}
