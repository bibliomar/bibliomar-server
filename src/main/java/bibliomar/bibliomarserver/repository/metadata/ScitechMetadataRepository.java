package bibliomar.bibliomarserver.repository.metadata;

import bibliomar.bibliomarserver.model.metadata.ScitechMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ScitechMetadataRepository extends CrudRepository<ScitechMetadata, Integer> {
    ScitechMetadata findByMD5(String MD5);

    boolean existsByMD5(String MD5);

}
