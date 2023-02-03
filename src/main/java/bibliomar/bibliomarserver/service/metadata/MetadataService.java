package bibliomar.bibliomarserver.service.metadata;

import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import bibliomar.bibliomarserver.model.metadata.ScitechMetadata;
import bibliomar.bibliomarserver.repository.metadata.FictionMetadataRepository;
import bibliomar.bibliomarserver.repository.metadata.ScitechMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MetadataService {


    private FictionMetadataRepository fictionMetadataRepository;
    private ScitechMetadataRepository scitechMetadataRepository;


    @Autowired
    public MetadataService(FictionMetadataRepository fictionMetadataRepository,
                           ScitechMetadataRepository scitechMetadataRepository) {
        this.fictionMetadataRepository = fictionMetadataRepository;
        this.scitechMetadataRepository = scitechMetadataRepository;
    }

    @Async
    public CompletableFuture<FictionMetadata> getFictionMetadata(String md5) {
        FictionMetadata possibleFictionMetadata = fictionMetadataRepository.findByMD5(md5);
        if (possibleFictionMetadata == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Metadata does not exists");
        } else if (possibleFictionMetadata.isMetadataInvalid()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Metadata exists but is not valid.");
        }

        return CompletableFuture.completedFuture(possibleFictionMetadata);
    }

    @Async
    public CompletableFuture<List<FictionMetadata>> getListFictionMetadata(List<String> MD5List) {
        List<FictionMetadata> possibleFictionMetadata = fictionMetadataRepository.findAllByMD5In(MD5List);
        if (possibleFictionMetadata == null || possibleFictionMetadata.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No metadata found for MD5s in list");
        }

        return CompletableFuture.completedFuture(possibleFictionMetadata);
    }

    @Async
    public CompletableFuture<ScitechMetadata> getScitechMetadata(String md5) {
        ScitechMetadata possibleScitechMetadata = scitechMetadataRepository.findByMD5(md5);
        if (possibleScitechMetadata == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No metadata found in MD5s in list");
        } else if (possibleScitechMetadata.isMetadataInvalid()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Metadata exists but is not valid.");
        }

        return CompletableFuture.completedFuture(possibleScitechMetadata);
    }

    @Async
    public CompletableFuture<List<ScitechMetadata>> getListScitechMetadata(List<String> MD5List) {
        List<ScitechMetadata> possibleScitechMetadata = scitechMetadataRepository.findAllByMD5In(MD5List);
        if (possibleScitechMetadata == null || possibleScitechMetadata.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No metadata found for MD5s in list");
        }

        return CompletableFuture.completedFuture(possibleScitechMetadata);
    }


}
