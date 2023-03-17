package bibliomar.bibliomarserver.model.metadata;

import bibliomar.bibliomarserver.utils.constants.Topics;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Getter
@Setter
@Table(name = "updated")
public class ScitechMetadata extends Metadata {

    @Column(name = "Identifier", columnDefinition = "VARCHAR(300)")
    protected String identifier;

    @Column(name = "VolumeInfo", columnDefinition = "VARCHAR(100)")
    private String volumeInfo;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "MD5", referencedColumnName = "MD5", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScitechDescription description;

    public ScitechMetadata() {
        this.setTopic(Topics.scitech);
    }

    @JsonGetter("description")
    public String getDescriptionString() {
        if (this.description == null || this.description.getDescription().isBlank()) {
            return null;
        }
        return this.description.getDescription();
    }

}
