package bibliomar.bibliomarserver.service.metadata;

import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import bibliomar.bibliomarserver.repository.metadata.FictionMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MetadataService {


    private FictionMetadataRepository fictionMetadataRepository;

    @Autowired
    public MetadataService(FictionMetadataRepository fictionMetadataRepository) {
        this.fictionMetadataRepository = fictionMetadataRepository;
    }

    public FictionMetadata getFictionMetadata(String md5) {
        FictionMetadata possibleFictionMetadata = fictionMetadataRepository.findByMD5(md5);
        if (possibleFictionMetadata == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Metadata does not exists");
        } else if (!possibleFictionMetadata.isMetadataValid()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Metadata exists but is not valid.");
        }

        return possibleFictionMetadata;
    }

}
