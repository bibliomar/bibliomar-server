package bibliomar.bibliomarserver.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.models.library.UserLibrary;

public interface UserLibraryRepository extends CrudRepository<UserLibrary, String> {

    List<UserLibrary> findAll();

    boolean existsByUsername(String username);

    UserLibrary findByUsername(String username);

    UserLibrary save(UserLibrary userLibrary);
}
