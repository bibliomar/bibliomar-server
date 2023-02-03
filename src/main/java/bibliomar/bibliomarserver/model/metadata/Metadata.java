package bibliomar.bibliomarserver.model.metadata;

import bibliomar.bibliomarserver.utils.MD5;
import bibliomar.bibliomarserver.utils.contants.Topics;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    // TODO: Implement coverUrl field pointing to libgen's cover images.
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
    protected LocalDateTime timeAdded;
    @Column(name = "TimeLastModified")
    protected LocalDateTime timeLastModified;

    @Column(name = "Extension")
    protected String extension;
    @Column(name = "Filesize")
    protected int fileSize;

    @Column(name = "Pages")
    protected String pages;

    @Column(name = "Coverurl")
    @JsonIgnore
    protected String coverReference;

    @Column(name = "Series")
    protected String series;
    @Column(name = "Edition")
    protected String edition;
    @Column(name = "Language")
    protected String language;
    @Column(name = "Year")
    protected String year;
    @Column(name = "Publisher")
    protected String publisher;

    @Transient
    protected Topics topic;

    @Transient
    @JsonProperty("coverURL")
    protected String coverURL;

    @Transient
    @JsonProperty("downloadMirrors")
    protected MetadataDownloadMirrors downloadMirrors;

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
        this.setCoverReference(baseMetadata.getCoverReference());
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
        this.setTopic(baseMetadata.getTopic());
    }

    public String getMD5() {
        return this.MD5;
    }

    @JsonIgnore
    public MD5 getMD5AsType() {
        return new MD5(this.MD5);
    }

    @JsonGetter("coverURL")
    public String getCoverURL() {
        this.buildCoverUrl();
        return this.coverURL;
    }

    @JsonGetter("downloadMirrors")
    public MetadataDownloadMirrors getDownloadMirrors() {
        this.buildMetadataDownloadMirrors();
        return this.downloadMirrors;
    }

    public void buildCoverUrl() {
        if (coverURL != null && !coverURL.isBlank()) {
            return;
        }
        String libgenBaseUrl = "https://libgen.is";
        if (this.coverReference == null || this.coverReference.isBlank()) {
            this.coverURL = null;
            return;
        }

        if (topic == Topics.fiction) {
            this.coverURL = libgenBaseUrl + "/fictioncovers/" + this.coverReference;
        } else {
            this.coverURL = libgenBaseUrl + "/covers/" + this.coverReference;
        }
    }

    public void buildMetadataDownloadMirrors() {
        if (this.downloadMirrors != null) {
            return;
        }
        this.downloadMirrors = new MetadataDownloadMirrors(this.MD5, this.topic);
    }

}
