package bibliomar.bibliomarserver.model.user.forms;

import bibliomar.bibliomarserver.model.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterForm {
    @NotNull
    @NotBlank(message = "Username cannot be blank")
    String username;
    @NotNull
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!#%*?&]{6,16}$", message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    String password;
    @NotNull
    @Email
    @NotBlank(message = "Email cannot be blank")
    String email;

    @NotNull(message = "User must have a role specified")
    UserRole role = UserRole.ROLE_USER;
}
