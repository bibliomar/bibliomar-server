package bibliomar.bibliomarserver.repository.metadata;

import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface FictionMetadataRepository extends CrudRepository<FictionMetadata, Integer> {
    FictionMetadata findByMD5(String MD5);

    boolean existsByMD5(String MD5);

}
