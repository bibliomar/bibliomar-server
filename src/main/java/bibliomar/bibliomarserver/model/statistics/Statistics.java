package bibliomar.bibliomarserver.model.statistics;

import bibliomar.bibliomarserver.model.metadata.FictionMetadata;
import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.model.metadata.ScitechMetadata;
import bibliomar.bibliomarserver.utils.contants.Topics;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Getter
@Setter
@Table(name = "statistics")
public class Statistics {
    @Id
    @Column(name = "MD5", nullable = false)
    private String MD5;

    @Column(name = "topic", nullable = false)
    private Topics topic;

    @Column(name = "views", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    private Long numOfViews = 0L;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FictionMetadata fictionMetadataReference;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ScitechMetadata scitechMetadataReference;

    @Transient
    @JsonProperty("metadata")
    private Metadata metadata;


    public void incrementViews() {
        this.numOfViews += 1;
    }


    public static Statistics build(Metadata metadata) {
        Statistics statistics = new Statistics();
        statistics.setMD5(metadata.getMD5());
        statistics.setTopic(metadata.getTopic());

        if (metadata instanceof FictionMetadata && metadata.getTopic() == Topics.fiction) {
            statistics.setFictionMetadataReference((FictionMetadata) metadata);
        } else if (metadata instanceof ScitechMetadata && metadata.getTopic() == Topics.scitech) {
            statistics.setScitechMetadataReference((ScitechMetadata) metadata);
        }

        return statistics;
    }

    public void buildMetadata() {
        if (this.topic == Topics.fiction && this.fictionMetadataReference != null) {
            this.metadata = this.fictionMetadataReference;
        } else if (this.topic == Topics.scitech && this.scitechMetadataReference != null) {
            this.metadata = this.scitechMetadataReference;
        }
    }

    @JsonGetter("metadata")
    public Metadata getMetadata() {
        this.buildMetadata();
        return this.metadata;
    }


}
