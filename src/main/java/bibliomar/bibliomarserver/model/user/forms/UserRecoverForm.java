package bibliomar.bibliomarserver.model.user.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRecoverForm {
    @Email
    @NotNull
    private String email;
}
