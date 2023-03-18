package bibliomar.bibliomarserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bibliomar.bibliomarserver.models.library.UserLibrary;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, String> {

    List<UserLibrary> findAll();

    Optional<UserLibrary> findByUsername(String username);

}
