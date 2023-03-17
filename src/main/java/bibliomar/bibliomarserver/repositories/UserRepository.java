package bibliomar.bibliomarserver.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.models.user.User;

public interface UserRepository extends CrudRepository<User, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);


    List<User> findAll();

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    User save(User user);

}
