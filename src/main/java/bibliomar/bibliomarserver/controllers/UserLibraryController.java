package bibliomar.bibliomarserver.controllers;

import bibliomar.bibliomarserver.config.UserDetailsServiceImpl;
import bibliomar.bibliomarserver.models.library.UserLibrary;
import bibliomar.bibliomarserver.models.library.UserLibraryEntry;
import bibliomar.bibliomarserver.models.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.models.library.forms.UserLibraryRemoveEntryForm;
import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.repositories.UserRepository;
import bibliomar.bibliomarserver.services.UserLibraryService;
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
    private UserRepository userRepo;

    @Autowired
    public UserLibraryController(UserLibraryService userLibraryService) {
        this.userLibraryService = userLibraryService;
    }

    @GetMapping
    public ResponseEntity<UserLibrary> getUserLibrary() throws ExecutionException, InterruptedException {
        User user = this.getAuthenticatedUser();
        return ResponseEntity.ok(userLibraryService.getUserLibrary(user.getUserLibrary().getId()).get());
    }

    @GetMapping("/{MD5}")
    public ResponseEntity<UserLibraryEntry> getUserLibraryEntry(@Valid MD5 md5) throws ExecutionException, InterruptedException {
        User user = this.getAuthenticatedUser();
        return ResponseEntity.ok(userLibraryService.getUserLibraryEntry(user.getUserLibrary().getId(), md5).get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToUserLibrary(@Valid @RequestBody UserLibraryAddEntryForm addEntryForm) throws ExecutionException, InterruptedException {
        User user = this.getAuthenticatedUser();
        userLibraryService.addOrMoveEntry(user.getUserLibrary().getId(), addEntryForm).get();
    }

    @DeleteMapping("/{MD5}")
    public void removeFromUserLibrary(@Valid MD5 md5) throws ExecutionException, InterruptedException {
        User user = this.getAuthenticatedUser();
        userLibraryService.removeEntry(user.getUserLibrary().getId(), md5).get();
    }

    private User getAuthenticatedUser() {
        UserDetails userDetails = UserDetailsServiceImpl.getAuthenticatedUser();
        return this.userRepo.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername()).get();
    }

}
