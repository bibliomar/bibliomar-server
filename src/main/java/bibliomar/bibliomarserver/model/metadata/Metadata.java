package bibliomar.bibliomarserver.model.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
public class Metadata {
    @Id
    @Column(name = "ID")
    protected long id;
    @Column(name = "MD5", columnDefinition = "CHAR(32)")
    protected String MD5;
    @Column(name = "Title")
    protected String title;

    @Column(name = "Author")
    protected String author;

    @Column(name = "TimeAdded")
    private LocalDateTime timeAdded;
    @Column(name = "TimeLastModified")
    private LocalDateTime timeLastModified;

    @Column(name = "Extension")
    private String extension;
    @Column(name = "Filesize")
    private int fileSize;

    @Column(name = "Pages")
    private String pages;

    @Column(name = "Coverurl")
    private String coverURL;

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


    /*
     * There's a few values that should be mandatory for a given Metadata, namely:
     * - MD5
     * - Title
     * - Author
     * It doesn't matter which value is missing, a metadata is deemed invalid if any of those is.
     */
    @JsonIgnore
    public boolean isMetadataInvalid() {

        if (this.MD5 == null || this.MD5.isBlank()) {
            return true;
        }
        if (this.title == null || this.title.isBlank()) {
            return true;
        }
        if (this.author == null || this.author.isBlank()) {
            return true;
        }

        return false;

    }

    /**
     * This method imports the metadata from another Metadata object.
     * It can also be used by classes that inherit from Metadata to import the
     * metadata from a base Metadata object.
     *
     * @param baseMetadata The metadata to import from.
     */
    public void importMetadata(Metadata baseMetadata) {
        this.setTitle(baseMetadata.getTitle());
        this.setAuthor(baseMetadata.getAuthor());
        this.setMD5(baseMetadata.getMD5());
        this.setCoverURL(baseMetadata.getCoverURL());
        this.setEdition(baseMetadata.getEdition());
        this.setExtension(baseMetadata.getExtension());
        this.setFileSize(baseMetadata.getFileSize());
        this.setLanguage(baseMetadata.getLanguage());
        this.setPublisher(baseMetadata.getPublisher());
        this.setSeries(baseMetadata.getSeries());
        this.setPages(baseMetadata.getPages());
        this.setTimeAdded(baseMetadata.getTimeAdded());
        this.setTimeLastModified(baseMetadata.getTimeLastModified());
        this.setYear(baseMetadata.getYear());
    }

}
