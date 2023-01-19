package bibliomar.bibliomarserver.model.library;

import java.util.HashMap;
import java.util.Hashtable;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_library")
public class UserLibrary {
    @Id
    private String username;

    @NotNull
    @Column(name = "reading", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, Object> reading = new HashMap<String, Object>();
    @NotNull
    @Column(name = "to_read", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, Object> toRead = new HashMap<String, Object>();
    @NotNull
    @Column(name = "finished", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, Object> finished = new HashMap<String, Object>();
    @NotNull
    @Column(name = "backlog", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, Object> backlog = new HashMap<String, Object>();
    @NotNull
    @Column(name = "dropped", columnDefinition = "json")
    @Type(JsonType.class)
    private HashMap<String, Object> dropped = new HashMap<String, Object>();
}
