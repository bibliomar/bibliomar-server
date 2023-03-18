package bibliomar.bibliomarserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bibliomar.bibliomarserver.models.library.UserLibrary;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

    List<UserLibrary> findAll();

}
