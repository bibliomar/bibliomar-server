package bibliomar.bibliomarserver.services;

import bibliomar.bibliomarserver.models.metadata.FictionMetadata;
import bibliomar.bibliomarserver.models.metadata.ScitechMetadata;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MetadataService {

    public CompletableFuture<FictionMetadata> getFictionMetadata(String md5);
    public CompletableFuture<List<FictionMetadata>> getListFictionMetadata(List<String> MD5List);
    public CompletableFuture<ScitechMetadata> getScitechMetadata(String md5);
    public CompletableFuture<List<ScitechMetadata>> getListScitechMetadata(List<String> MD5List);


}
