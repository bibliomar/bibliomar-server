package bibliomar.bibliomarserver.service.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import bibliomar.bibliomarserver.model.security.JwtTokenResponse;
import bibliomar.bibliomarserver.model.user.UserDetailsImpl;
import bibliomar.bibliomarserver.model.user.forms.UserLoginForm;
import bibliomar.bibliomarserver.model.user.forms.UserRecoverForm;
import bibliomar.bibliomarserver.model.user.forms.UserUpdateForm;
import bibliomar.bibliomarserver.service.mail.MailService;
import bibliomar.bibliomarserver.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.model.user.forms.UserRegisterForm;
import bibliomar.bibliomarserver.repository.user.UserRepository;

@Service
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void checkExistingUser(UserRegisterForm registerForm) {
        User possibleExistingUser = userRepository.findByUsernameOrEmail(registerForm.getUsername(),
                registerForm.getEmail());

        if (possibleExistingUser != null) {
            if (possibleExistingUser.getUsername().equals(registerForm.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
            }
            if (possibleExistingUser.getEmail().equals(registerForm.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
        }
    }

    @Async
    public CompletableFuture<Void> registerUser(UserRegisterForm registerForm) {
        this.checkExistingUser(registerForm);

        try {
            User newUser = new User();
            newUser.setUsername(registerForm.getUsername());
            newUser.setEmail(registerForm.getEmail());
            newUser.setRole(registerForm.getRole());
            String hashedPassword = passwordEncoder.encode(registerForm.getPassword());
            newUser.setPassword(hashedPassword);

            UserLibrary newUserLibrary = new UserLibrary();
            newUserLibrary.setUsername(newUser.getUsername());
            newUser.setUserLibrary(newUserLibrary);
            this.userRepository.save(newUser);
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            // TODO: Better handle exceptions
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while registering user");
        }

    }

    @Async
    public CompletableFuture<User> getPublicUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with given username.");
        }
        if (user.isPrivateProfile()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has a private profile.");
        }

        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<JwtTokenResponse> authUser(UserLoginForm loginForm) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getUsername(),
                loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtTokenUtils.generateToken(userDetails.getUsername());
        JwtTokenResponse jwtTokenResponse = JwtTokenResponse.build(jwtTokenUtils.verifyToken(jwtToken));
        return CompletableFuture.completedFuture(jwtTokenResponse);

    }

    @Async
    public CompletableFuture<Void> updateUser(UserDetails user, UserUpdateForm updateForm){
        User userToUpdate = userRepository.findByUsername(user.getUsername());
        if (userToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with given username.");
        }

        if (updateForm.getNewUsername() != null) {
            User possibleExistingUser = userRepository.findByUsername(updateForm.getNewUsername());
            if (possibleExistingUser != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
            }
            userToUpdate.setUsername(updateForm.getNewUsername());
        }

        if (updateForm.getNewPassword() != null) {
            if (!updateForm.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!#%*?&]{6,16}$")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be between 6 and 16 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character.");
            }
            String hashedPassword = passwordEncoder.encode(updateForm.getNewPassword());
            userToUpdate.setPassword(hashedPassword);
        }

        if (updateForm.getNewEmail() != null) {
            User possibleExistingUser = userRepository.findByEmail(updateForm.getNewEmail());
            if (possibleExistingUser != null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
            userToUpdate.setEmail(updateForm.getNewEmail());
        }



        if (updateForm.isTogglePrivateProfile()){
            userToUpdate.setPrivateProfile(!userToUpdate.isPrivateProfile());
        }

        userRepository.save(userToUpdate);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> sendRecoveryEmail(UserRecoverForm recoverForm) {
        User possibleExistingUser = userRepository.findByEmail(recoverForm.getEmail());
        if (possibleExistingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email doesn't correspond to any user.");
        }

        String jwtPasswordlessToken = jwtTokenUtils.generatePasswordlessToken(possibleExistingUser.getUsername());

        try {
            this.mailService.sendRecoveryMail(possibleExistingUser, jwtPasswordlessToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while sending recovery email.", e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
