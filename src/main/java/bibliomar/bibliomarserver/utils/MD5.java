package bibliomar.bibliomarserver.utils;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MD5 {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-fA-F0-9]{32}$", message = "MD5 must be a 32 character hexadecimal string")
    private String MD5;

    public MD5(String MD5) {
        this.MD5 = MD5;
    }
}
