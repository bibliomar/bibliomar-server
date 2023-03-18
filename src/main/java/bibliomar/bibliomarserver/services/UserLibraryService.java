package bibliomar.bibliomarserver.services;

import bibliomar.bibliomarserver.models.library.UserLibrary;
import bibliomar.bibliomarserver.models.library.UserLibraryEntry;
import bibliomar.bibliomarserver.models.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.models.metadata.Metadata;

import bibliomar.bibliomarserver.utils.MD5;

import java.util.concurrent.CompletableFuture;

public interface UserLibraryService {

   
    public UserLibrary retrieveExistingUserLibrary(long id);
    public CompletableFuture<Void> removeEntry(long id, MD5 md5);
    public void appendToLibrary(UserLibrary userLibrary, Metadata metadata, String targetCategory);
    public CompletableFuture<Void> addOrMoveEntry(long id, UserLibraryAddEntryForm addEntryForm);
    public CompletableFuture<UserLibrary> getUserLibrary(long id);
    public CompletableFuture<UserLibraryEntry> getUserLibraryEntry(long id, MD5 md5);

}
