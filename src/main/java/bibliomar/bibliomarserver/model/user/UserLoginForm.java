package bibliomar.bibliomarserver.model.user;

import lombok.Data;

@Data
public class UserLoginForm {
    private String username;
    private String password;
}
