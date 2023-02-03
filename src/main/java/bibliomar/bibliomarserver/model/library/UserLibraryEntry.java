package bibliomar.bibliomarserver.model.library;

import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.utils.contants.Topics;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLibraryEntry extends Metadata {

    @Transient
    @JsonProperty
    private Topics topic;

    @NotNull
    @NotEmpty
    private String category;

    public UserLibraryEntry(Metadata baseMetadata) {
        this.importMetadata(baseMetadata);
    }

}
