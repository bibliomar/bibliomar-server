package bibliomar.bibliomarserver.services;

import java.util.concurrent.CompletableFuture;

import bibliomar.bibliomarserver.config.jwt.JwtTokenResponse;
import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.models.user.forms.UserLoginForm;
import bibliomar.bibliomarserver.models.user.forms.UserRecoveryForm;
import bibliomar.bibliomarserver.models.user.forms.UserRegisterForm;
import bibliomar.bibliomarserver.models.user.forms.UserUpdateForm;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    public void checkExistingUser(UserRegisterForm registerForm);
    public CompletableFuture<Void> registerUser(UserRegisterForm registerForm);
    public CompletableFuture<User> getPublicUser(String username);
    public CompletableFuture<JwtTokenResponse> authUser(UserLoginForm loginForm);

    CompletableFuture<Void> verifyUser(String token);

    public CompletableFuture<Void> updateUser(UserDetails user, UserUpdateForm updateForm);
    public CompletableFuture<Void> sendRecoveryEmail(UserRecoveryForm recoverForm);
    CompletableFuture<Void> sendVerificationEmail(UserRecoveryForm recoverForm);
}
