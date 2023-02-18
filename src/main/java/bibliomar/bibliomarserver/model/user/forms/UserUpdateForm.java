package bibliomar.bibliomarserver.model.user.forms;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateForm {
    private String newUsername;

    private String newEmail;

    private String newPassword;


    private boolean togglePrivateProfile;
}
