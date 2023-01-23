package bibliomar.bibliomarserver.controller.library;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.library.forms.UserLibraryAddEntryForm;
import bibliomar.bibliomarserver.model.user.UserDetailsImpl;
import bibliomar.bibliomarserver.service.library.UserLibraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<UserLibrary>> getAllUserLibraries() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(userLibraryService.getAllUserLibraries().get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addToUserLibrary(@Valid @RequestBody UserLibraryAddEntryForm addEntryForm) throws ExecutionException, InterruptedException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userLibraryService.addOrMoveEntry(user.getUsername(), addEntryForm).get();
    }

}
