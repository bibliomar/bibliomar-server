package bibliomar.bibliomarserver.repository.library;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.model.library.UserLibrary;

public interface UserLibraryRepository extends CrudRepository<UserLibrary, String> {

    List<UserLibrary> findAll();

    boolean existsByUsername(String username);

    UserLibrary findByUsername(String username);

    UserLibrary save(UserLibrary userLibrary);
}
