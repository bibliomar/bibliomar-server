package bibliomar.bibliomarserver.model.library;

import bibliomar.bibliomarserver.model.metadata.Metadata;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLibraryEntry extends Metadata {

    @NotNull
    @NotEmpty
    private String category;

    public UserLibraryEntry(Metadata baseMetadata) {
        this.importMetadata(baseMetadata);
    }

}
