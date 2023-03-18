package bibliomar.bibliomarserver.model.metadata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "fiction_description", schema = "libgen20_fiction")
@Getter
@Setter
public class FictionDescription {
    @Id
    @Column(name = "MD5", columnDefinition = "CHAR(32)")
    private String MD5;
    @Column(name = "Descr")
    private String description;
}
