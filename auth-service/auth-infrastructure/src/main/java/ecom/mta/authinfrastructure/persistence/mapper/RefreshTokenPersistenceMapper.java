package ecom.mta.authinfrastructure.persistence.mapper;

import ecom.mta.authdomain.model.RefreshToken;
import ecom.mta.authinfrastructure.persistence.entity.RefreshTokenJpaEntity;

import ecom.mta.authdomain.model.RefreshTokenId;
import ecom.mta.authdomain.model.UserId;

public final class RefreshTokenPersistenceMapper {

    private RefreshTokenPersistenceMapper() {
        throw new AssertionError("No RefreshTokenPersistenceMapper instances for you!");
    }


    public static RefreshTokenJpaEntity toEntity(RefreshToken token) {
        if (token == null) {
            return null;
        }

        return new RefreshTokenJpaEntity(
                token.getId() != null ? token.getId().value() : null,
                token.getUserId() != null ? token.getUserId().value() : null,
                token.getTokenHash(),
                token.getExpiresAt(),
                token.getRevokedAt(),
                token.getCreatedAt(), token.getReplacedByTokenId() == null ? null : token.getReplacedByTokenId().value()
        );
    }


    public static RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RefreshToken(
                RefreshTokenId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getCreatedAt(), entity.getReplacedByTokenId() == null ? null : RefreshTokenId.of(entity.getReplacedByTokenId())
        );
    }
}
