package ecom.mta.authinfrastructure.persistence.mapper;
import ecom.mta.authdomain.model.Email;
import ecom.mta.authdomain.model.User;
import ecom.mta.authdomain.model.UserId;
import ecom.mta.authdomain.model.UserStatus;
import ecom.mta.authinfrastructure.persistence.entity.UserJpaEntity;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
        throw new AssertionError("No UserPersistenceMapper instances for you!");
    }

    public static UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserJpaEntity(
                user.getId() != null ? user.getId().value() : null,
                user.getEmail().value(),
                user.getPasswordHash(),
                user.getStatus().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(), user.getEmailVerifiedAt(), user.getFailedLoginAttempts(),
                user.getLockedUntil(), user.getPasswordChangedAt(), user.getLastLoginAt()
        );
    }
    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getId() == null) {
            throw new IllegalStateException("Persisted User entity must have an ID.");
        }

        return new User(
                UserId.of(entity.getId()),
                new Email(entity.getEmail()),
                entity.getPasswordHash(),
                UserStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getEmailVerifiedAt(), entity.getFailedLoginAttempts(),
                entity.getLockedUntil(), entity.getPasswordChangedAt(), entity.getLastLoginAt()
        );
    }
}
