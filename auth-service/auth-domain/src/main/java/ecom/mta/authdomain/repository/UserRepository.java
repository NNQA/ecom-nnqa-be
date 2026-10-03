package ecom.mta.authdomain.repository;

import ecom.mta.authdomain.model.Email;
import ecom.mta.authdomain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(Email email);
    User save(User user);
}
