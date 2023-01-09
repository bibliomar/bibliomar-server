package bibliomar.bibliomarserver.repository.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.model.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User save(User user);

}
