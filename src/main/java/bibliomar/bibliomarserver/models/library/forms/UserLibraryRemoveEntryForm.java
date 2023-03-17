package bibliomar.bibliomarserver.models.library.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLibraryRemoveEntryForm {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "MD5 must be a 32 character hexadecimal string")
    private String md5;
}
