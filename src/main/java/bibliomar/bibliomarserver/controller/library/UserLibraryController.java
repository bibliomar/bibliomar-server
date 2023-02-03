package bibliomar.bibliomarserver.controller.library;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.library.UserLibraryEntry;
import bibliomar.bibliomarserver.model.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.model.library.forms.UserLibraryRemoveEntryForm;
import bibliomar.bibliomarserver.service.library.UserLibraryService;
import bibliomar.bibliomarserver.service.user.UserDetailsServiceImpl;
import bibliomar.bibliomarserver.utils.MD5;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/library")
public class UserLibraryController {

    private final UserLibraryService userLibraryService;

    @Autowired
    public UserLibraryController(UserLibraryService userLibraryService) {
        this.userLibraryService = userLibraryService;
    }

    @GetMapping
    public ResponseEntity<UserLibrary> getUserLibrary() throws ExecutionException, InterruptedException {
        UserDetails user = UserDetailsServiceImpl.getAuthenticatedUser();
        return ResponseEntity.ok(userLibraryService.getUserLibrary(user.getUsername()).get());
    }

    @GetMapping("/{MD5}")
    public ResponseEntity<UserLibraryEntry> getUserLibraryEntry(@Valid MD5 md5) throws ExecutionException, InterruptedException {
        UserDetails user = UserDetailsServiceImpl.getAuthenticatedUser();
        return ResponseEntity.ok(userLibraryService.getUserLibraryEntry(user.getUsername(), md5).get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToUserLibrary(@Valid @RequestBody UserLibraryAddEntryForm addEntryForm) throws ExecutionException, InterruptedException {
        UserDetails user = UserDetailsServiceImpl.getAuthenticatedUser();
        userLibraryService.addOrMoveEntry(user.getUsername(), addEntryForm).get();
    }

    @DeleteMapping("/{MD5}")
    public void removeFromUserLibrary(@Valid MD5 md5) throws ExecutionException, InterruptedException {
        UserDetails user = UserDetailsServiceImpl.getAuthenticatedUser();
        userLibraryService.removeEntry(user.getUsername(), md5).get();
    }

}
