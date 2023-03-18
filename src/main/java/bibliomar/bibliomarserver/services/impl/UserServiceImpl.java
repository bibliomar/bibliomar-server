package bibliomar.bibliomarserver.services.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import bibliomar.bibliomarserver.config.jwt.JwtTokenResponse;
import bibliomar.bibliomarserver.models.library.UserLibrary;
import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.models.user.UserDetailsImpl;
import bibliomar.bibliomarserver.models.user.forms.UserLoginForm;
import bibliomar.bibliomarserver.models.user.forms.UserEmailForm;
import bibliomar.bibliomarserver.models.user.forms.UserRegisterForm;
import bibliomar.bibliomarserver.models.user.forms.UserUpdateForm;
import bibliomar.bibliomarserver.repositories.UserRepository;
import bibliomar.bibliomarserver.services.MailService;
import bibliomar.bibliomarserver.services.TokenService;
import bibliomar.bibliomarserver.services.UserService;
import bibliomar.bibliomarserver.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

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

    @Autowired
    private TokenService tokenService;

    @Override
    public void checkExistingUser(UserRegisterForm registerForm) {
        Optional<User> possibleExistingUser = userRepository.findByUsernameOrEmail(registerForm.getUsername(),
                registerForm.getEmail());

        if (possibleExistingUser.isPresent()) {
            User existingUser = possibleExistingUser.get();
            if (existingUser.getUsername().equals(registerForm.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
            }
            if (existingUser.getEmail().equals(registerForm.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
        }
    }

    @Async
    @Override
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

            // Sends the verification email
            UserEmailForm verificationForm = new UserEmailForm();
            verificationForm.setEmail(newUser.getEmail());

            this.sendVerificationEmail(verificationForm).get();

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            // TODO: Better handle exceptions
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while registering user");
        }

    }

    @Async
    @Override
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
    @Override
    public CompletableFuture<JwtTokenResponse> authUser(UserLoginForm loginForm) {
        // Retrieves the user using the specified username or email
        // If it's an email, the user's username is used instead
        Optional<User> possibleExistingUser = userRepository.findByUsernameOrEmail(loginForm.getUsernameOrEmail(), loginForm.getUsernameOrEmail());
        if (possibleExistingUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with given username.");
        } else if (possibleExistingUser.get().isPreMigration()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User should ask for password reset");
        } else if (!possibleExistingUser.get().isVerified()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User should verify email");
        }

        // Allows the user to login if the loginForm.usernameOrEmail is an email
        String usableLoginCredentials = possibleExistingUser.get().getUsername();

        // Make sure to use the loginForm's password, not the user's.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usableLoginCredentials,
                loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.isCredentialsNonExpired()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User should ask for password reset");
        }
        String jwtToken = jwtTokenUtils.generateToken(userDetails.getUsername());
        JwtTokenResponse jwtTokenResponse = JwtTokenResponse.build(jwtTokenUtils.verifyToken(jwtToken));
        return CompletableFuture.completedFuture(jwtTokenResponse);

    }

    @Async
    @Override
    public CompletableFuture<Void> verifyUser(String token) {
        // The validateToken method handles validation and throws exceptions accordingly if needed
        User verifiedUser = tokenService.validateToken(token);
        verifiedUser.setVerified(true);
        userRepository.save(verifiedUser);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
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
            if (userToUpdate.isPreMigration()){
                userToUpdate.setPreMigration(false);
            }
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
    @Override
    public CompletableFuture<Void> sendRecoveryEmail(UserEmailForm recoverForm) {
        User possibleExistingUser = userRepository.findByEmail(recoverForm.getEmail());
        if (possibleExistingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email doesn't correspond to any user.");
        }

        String tokenValue = this.tokenService.generateToken(possibleExistingUser);          

        try {
            this.mailService.sendRecoveryMail(possibleExistingUser, tokenValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while sending recovery email.", e);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> sendVerificationEmail(UserEmailForm emailForm) {
        User possibleExistingUser = userRepository.findByEmail(emailForm.getEmail());
        if (possibleExistingUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email doesn't belong to any user.");
        } else if (possibleExistingUser.isVerified()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already verified.");
        }
        String verificationToken = tokenService.generateToken(possibleExistingUser);

        try {
            this.mailService.sendVerificationMail(possibleExistingUser, verificationToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while sending verification email.", e);
        }

        return CompletableFuture.completedFuture(null);
    }

}
