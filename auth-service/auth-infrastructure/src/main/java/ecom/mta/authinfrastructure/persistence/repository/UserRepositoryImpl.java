package ecom.mta.authinfrastructure.persistence.repository;

import ecom.mta.authdomain.model.Email;
import ecom.mta.authdomain.model.User;
import ecom.mta.authdomain.repository.UserRepository;
import ecom.mta.authinfrastructure.persistence.entity.UserJpaEntity;
import ecom.mta.authinfrastructure.persistence.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository repository;

    public UserRepositoryImpl(UserJpaRepository repository) {
        this.repository = repository;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        if (email == null) {
            return Optional.empty();
        }

        return repository.findByEmail(email.value())
                .map(UserPersistenceMapper::toDomain);
    }


    @Override
    @Transactional
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot save a null User aggregate.");
        }

        UserJpaEntity entityToSave = UserPersistenceMapper.toEntity(user);

        UserJpaEntity savedEntity = repository.save(entityToSave);

        return UserPersistenceMapper.toDomain(savedEntity);
    }
}