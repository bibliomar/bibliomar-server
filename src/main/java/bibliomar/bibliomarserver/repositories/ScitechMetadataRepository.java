package bibliomar.bibliomarserver.repositories;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.models.metadata.ScitechMetadata;

import java.util.List;

public interface ScitechMetadataRepository extends CrudRepository<ScitechMetadata, Integer> {
    ScitechMetadata findByMD5(String MD5);

    List<ScitechMetadata> findAllByMD5In(List<String> MD5s);


    boolean existsByMD5(String MD5);

}
