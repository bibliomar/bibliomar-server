package bibliomar.bibliomarserver.model.metadata;

import bibliomar.bibliomarserver.utils.contants.Topics;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "fiction", schema = "libgen20_fiction")
@Getter
@Setter
public class FictionMetadata extends Metadata {

    @Column(name = "Identifier", columnDefinition = "VARCHAR(400)")
    protected String identifier;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "MD5", referencedColumnName = "MD5", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FictionDescription description;

    public FictionMetadata() {
        this.setTopic(Topics.fiction);
    }

    @JsonGetter("description")
    public String getDescriptionString() {
        if (this.description == null || this.description.getDescription().isBlank()) {
            return null;
        }
        return this.description.getDescription();
    }

}
