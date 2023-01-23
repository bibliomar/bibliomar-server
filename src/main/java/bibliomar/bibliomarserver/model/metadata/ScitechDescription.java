package bibliomar.bibliomarserver.model.metadata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "description")
@Getter
@Setter
public class ScitechDescription {
    @Id
    @Column(name = "MD5", columnDefinition = "CHAR(32)")
    private String MD5;
    @Column(name = "Descr")
    private String description;
}
