package bibliomar.bibliomarserver.service.library;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.library.UserLibraryEntry;
import bibliomar.bibliomarserver.model.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.model.metadata.ScitechMetadata;
import bibliomar.bibliomarserver.repository.library.UserLibraryRepository;
import bibliomar.bibliomarserver.repository.metadata.FictionMetadataRepository;
import bibliomar.bibliomarserver.repository.metadata.ScitechMetadataRepository;
import bibliomar.bibliomarserver.utils.MD5;
import bibliomar.bibliomarserver.utils.contants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class UserLibraryService {
    private final UserLibraryRepository userLibraryRepository;
    private final FictionMetadataRepository fictionMetadataRepository;
    private final ScitechMetadataRepository scitechMetadataRepository;

    @Autowired
    public UserLibraryService(UserLibraryRepository userLibraryRepository,
                              FictionMetadataRepository fictionMetadataRepository,
                              ScitechMetadataRepository scitechMetadataRepository) {
        this.userLibraryRepository = userLibraryRepository;
        this.fictionMetadataRepository = fictionMetadataRepository;
        this.scitechMetadataRepository = scitechMetadataRepository;
    }

    public UserLibrary retrieveExistingUserLibrary(String username) {
        if (!userLibraryRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "No library found for given username. " + "User may not be registered.");
        }
        return userLibraryRepository.findByUsername(username);
    }

    @Async
    public CompletableFuture<Void> removeEntry(String username, MD5 md5) {
        UserLibrary userLibrary = this.retrieveExistingUserLibrary(username);

        UserLibraryEntry possibleExistingEntry = userLibrary.getEntry(md5.getMD5());
        if (possibleExistingEntry == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No entry found with given MD5 in user library.");
        }

        try {
            userLibrary.removeEntry(possibleExistingEntry);
            this.userLibraryRepository.save(userLibrary);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while removing entry from user library.");
        }
        return CompletableFuture.completedFuture(null);
    }

    private void appendToLibrary(UserLibrary userLibrary, Metadata metadata, String targetCategory) {
        try {
            UserLibraryEntry newEntry = new UserLibraryEntry(metadata);
            newEntry.setCategory(targetCategory);
            userLibrary.appendEntry(newEntry);
            this.userLibraryRepository.save(userLibrary);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding entry to user library.");
        }
    }

    @Async
    public CompletableFuture<Void> addOrMoveEntry(String username, UserLibraryAddEntryForm addEntryForm) {
        UserLibrary userLibrary = this.retrieveExistingUserLibrary(username);
        String MD5 = addEntryForm.getMd5();
        Topics topic = addEntryForm.getTopic();
        String targetCategory = addEntryForm.getTargetCategory();

        UserLibraryEntry possibleExistingEntry = userLibrary.getEntry(MD5);
        if (possibleExistingEntry != null) {
            try {
                userLibrary.moveEntry(possibleExistingEntry, targetCategory);
                this.userLibraryRepository.save(userLibrary);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while moving entry in user library.");
            }

            return CompletableFuture.completedFuture(null);
        }

        switch (topic) {
            case fiction -> {
                if (!fictionMetadataRepository.existsByMD5(MD5)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No entry found with given MD5 on the " + topic + " topic.");
                }
                FictionMetadata fictionMetadata = fictionMetadataRepository.findByMD5(MD5);
                this.appendToLibrary(userLibrary, fictionMetadata, targetCategory);

            }
            case scitech -> {
                if (!scitechMetadataRepository.existsByMD5(MD5)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No book found with given MD5 on the " + topic + " topic.");
                }
                ScitechMetadata scitechMetadata = scitechMetadataRepository.findByMD5(MD5);
                this.appendToLibrary(userLibrary, scitechMetadata, targetCategory);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic.");
        }
        return CompletableFuture.completedFuture(null);

    }

    @Async
    public CompletableFuture<UserLibrary> getUserLibrary(String username) {
        UserLibrary userLibrary = this.retrieveExistingUserLibrary(username);
        return CompletableFuture.completedFuture(userLibrary);
    }

    @Async
    public CompletableFuture<UserLibraryEntry> getUserLibraryEntry(String username, MD5 md5) {
        this.retrieveExistingUserLibrary(username);
        UserLibrary userLibrary = userLibraryRepository.findByUsername(username);
        UserLibraryEntry possibleBook = userLibrary.getEntry(md5.getMD5());
        if (possibleBook == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No book found with given MD5.");
        }
        return CompletableFuture.completedFuture(possibleBook);
    }

}
