package bibliomar.bibliomarserver.model.metadata;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "fiction")
@Getter
@Setter
public class FictionMetadata extends Metadata {

    @Column(name = "Series")
    private String series;
    @Column(name = "Edition")
    private String edition;
    @Column(name = "Language")
    private String language;
    @Column(name = "Year")
    private String year;
    @Column(name = "Publisher")
    private String publisher;
    @Column(name = "Pages")
    private String pages;
    @Column(name = "Identifier")
    private String identifier;
    @Column(name = "GooglebookID")
    private String googleBooksID;
    @Column(name = "ASIN")
    private String ASIN;
    @Column(name = "Coverurl")
    private String coverURL;
    @Column(name = "Extension")
    private String extension;
    @Column(name = "Filesize")
    private int fileSize;

    @Column(name = "TimeAdded", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timeAdded;
    @Column(name = "TimeLastModified", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeLastModified;

    @ManyToOne
    @JoinColumn(name = "MD5", referencedColumnName = "MD5", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FictionDescription description;

    /*
     * There's a few values that should be mandatory for a given Metadata, namely:
     * - MD5
     * - Title
     * - Author
     * It doesn't matter which value is missing, but a error should be thrown if any is.
     */
    public boolean isMetadataValid() {

        if (this.MD5 == null || this.MD5.isBlank()) {
            return false;
        }
        if (this.title == null || this.title.isBlank()) {
            return false;
        }
        if (this.author == null || this.author.isBlank()) {
            return false;
        }

        return true;

    }

    @JsonGetter("description")
    public String getDescriptionString() {
        if (this.description == null || this.description.getDescription().isBlank()) {
            return null;
        }
        return this.description.getDescription();
    }

}
