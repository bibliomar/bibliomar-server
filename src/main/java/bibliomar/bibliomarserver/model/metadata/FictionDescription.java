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
@Table(name = "fiction_description")
@Getter
@Setter
public class FictionDescription {
    @Id
    @Column(name = "MD5", columnDefinition = "CHAR(32)")
    private String MD5;
    @Column(name = "Descr")
    private String description;

    @Column(name = "TimeLastModified", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeLastModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FictionDescription that = (FictionDescription) o;
        return MD5 != null && Objects.equals(MD5, that.MD5);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
