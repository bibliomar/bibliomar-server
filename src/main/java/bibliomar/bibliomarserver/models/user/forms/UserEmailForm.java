package bibliomar.bibliomarserver.models.user.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEmailForm {
    @Email
    @NotNull
    private String email;
}
