package bibliomar.bibliomarserver.model.metadata;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is the base class for all metadata.
 * It contains the basic information that all metadata entites should have.
 * It also contains the basic methods that all metadata entities should have.
 * <br>
 * The @MappedSuperclass annotation tells JPA that while this class is a Entity,
 * it should not have its own table.
 * <br><br>
 * It's unadvized to add a lot of properties here, since there may be historical discrepancies between
 * the Fiction and Scitech tables.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Metadata {
    @Id
    @Column(name = "ID")
    protected int id;
    @Column(name = "MD5", columnDefinition = "CHAR(32)")
    protected String MD5;
    @Column(name = "Title")
    protected String title;

    @Column(name = "Author")
    protected String author;

    public abstract boolean isMetadataValid();
}
