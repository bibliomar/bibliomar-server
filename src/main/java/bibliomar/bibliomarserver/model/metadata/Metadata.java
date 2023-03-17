package bibliomar.bibliomarserver.model.metadata;

import bibliomar.bibliomarserver.utils.MD5;
import bibliomar.bibliomarserver.utils.constants.Topics;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Instant timeAdded;

    @Column(name = "TimeLastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING)

    protected Instant timeLastModified;

    @Column(name = "Extension")
    protected String extension;
    @Column(name = "Filesize")
    protected int fileSize;

    @Column(name = "Pages")
    protected String pages;

    @Column(name = "Coverurl")
    protected String coverUrl;

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

    @Column(name = "ASIN", columnDefinition = "CHAR(10)")
    protected String ASIN;

    @Column(name = "GooglebookID", columnDefinition = "CHAR(45)")
    protected String googleBooksId;

    @Transient
    protected Topics topic;

    @Transient
    @JsonProperty("downloadMirrors")
    protected MetadataDownloadMirrors downloadMirrors;



    @JsonIgnore
    public boolean isMetadataInvalid() {

        if (this.MD5 == null || this.MD5.isBlank()) {
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
        this.setCoverUrl(baseMetadata.getCoverUrl());
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




    @JsonGetter("downloadMirrors")
    public MetadataDownloadMirrors getDownloadMirrors() {
        this.buildMetadataDownloadMirrors();
        return this.downloadMirrors;
    }

    public void buildMetadataDownloadMirrors() {
        if (this.downloadMirrors != null) {
            return;
        }
        this.downloadMirrors = new MetadataDownloadMirrors(this.MD5, this.topic);
    }

}
