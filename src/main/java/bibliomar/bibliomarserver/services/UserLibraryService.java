package bibliomar.bibliomarserver.services;

import bibliomar.bibliomarserver.models.library.UserLibrary;
import bibliomar.bibliomarserver.models.library.UserLibraryEntry;
import bibliomar.bibliomarserver.models.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.models.metadata.Metadata;

import bibliomar.bibliomarserver.utils.MD5;

import java.util.concurrent.CompletableFuture;

public interface UserLibraryService {

   
    public UserLibrary retrieveExistingUserLibrary(String usernameString);
    public CompletableFuture<Void> removeEntry(String username, MD5 md5);
    public void appendToLibrary(UserLibrary userLibrary, Metadata metadata, String targetCategory);
    public CompletableFuture<Void> addOrMoveEntry(String username, UserLibraryAddEntryForm addEntryForm);
    public CompletableFuture<UserLibrary> getUserLibrary(String username);
    public CompletableFuture<UserLibraryEntry> getUserLibraryEntry(String username, MD5 md5);

}
