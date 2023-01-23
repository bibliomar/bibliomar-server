package bibliomar.bibliomarserver.controller.user;

import java.util.List;
import java.util.concurrent.ExecutionException;

import bibliomar.bibliomarserver.model.security.JwtTokenResponse;
import bibliomar.bibliomarserver.model.user.forms.UserLoginForm;
import bibliomar.bibliomarserver.model.user.forms.UserRecoverForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.model.user.forms.UserRegisterForm;
import bibliomar.bibliomarserver.repository.library.UserLibraryRepository;
import bibliomar.bibliomarserver.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> login(@Valid @RequestBody UserLoginForm loginForm) throws ExecutionException, InterruptedException {
        JwtTokenResponse tokenResponse = userService.authUser(loginForm).get();
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void userRegister(@Valid @RequestBody UserRegisterForm registerForm, HttpServletRequest request) throws ExecutionException, InterruptedException {
        this.userService.registerUser(registerForm).get();
    }

    @PostMapping(path = "/recover", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void userRecover(@Valid @RequestBody UserRecoverForm recoverForm) throws ExecutionException, InterruptedException {
        this.userService.sendRecoveryEmail(recoverForm).get();
    }


    @GetMapping(path = "/all")
    public ResponseEntity<List<User>> getAllUsers() throws ExecutionException, InterruptedException {
        List<User> users = userService.getAllUsers().get();
        List<UserLibrary> userLibraries = userLibraryRepository.findAll();
        return ResponseEntity.ok(users);
    }


}
