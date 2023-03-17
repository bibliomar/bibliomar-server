package bibliomar.bibliomarserver.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import bibliomar.bibliomarserver.repositories.UserLibraryRepository;
import bibliomar.bibliomarserver.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import bibliomar.bibliomarserver.config.UserDetailsServiceImpl;
import bibliomar.bibliomarserver.config.jwt.JwtTokenResponse;
import bibliomar.bibliomarserver.models.library.UserLibrary;
import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.models.user.forms.UserLoginForm;
import bibliomar.bibliomarserver.models.user.forms.UserRecoverForm;
import bibliomar.bibliomarserver.models.user.forms.UserRegisterForm;
import bibliomar.bibliomarserver.models.user.forms.UserUpdateForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @GetMapping("/{username}")
    public ResponseEntity<User> getPublicUser(@PathVariable String username) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(userService.getPublicUser(username).get());
    }

    @PostMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateForm updateForm) throws ExecutionException, InterruptedException {
        UserDetails userDetails = UserDetailsServiceImpl.getAuthenticatedUser();
        return ResponseEntity.ok(userService.updateUser(userDetails, updateForm).get());
    }

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


}
