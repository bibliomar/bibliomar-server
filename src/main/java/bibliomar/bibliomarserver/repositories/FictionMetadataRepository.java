package bibliomar.bibliomarserver.repositories;

import org.springframework.data.repository.CrudRepository;

import bibliomar.bibliomarserver.models.metadata.FictionMetadata;

import java.util.List;

public interface FictionMetadataRepository extends CrudRepository<FictionMetadata, Integer> {
    FictionMetadata findByMD5(String MD5);

    List<FictionMetadata> findAllByMD5In(List<String> MD5List);

    boolean existsByMD5(String MD5);

}
