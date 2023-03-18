package bibliomar.bibliomarserver.models.user.forms;

import lombok.Data;

@Data
public class UserLoginForm {
    private String usernameOrEmail;
    private String password;
}
