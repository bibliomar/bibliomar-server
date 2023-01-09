package bibliomar.bibliomarserver.model.user;

import java.sql.Date;
import java.util.Hashtable;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @JsonIgnore
    private String password;
    @Email
    @NotNull
    private String email;

    @NotNull
    @Column(name = "reading", columnDefinition = "json")
    @Type(JsonType.class)
    private Hashtable<String, Object> reading = new Hashtable<String, Object>();
    @NotNull
    @Column(name = "toRead", columnDefinition = "json")
    @Type(JsonType.class)
    private Hashtable<String, Object> toRead = new Hashtable<String, Object>();
    @NotNull
    @Column(name = "finished", columnDefinition = "json")
    @Type(JsonType.class)
    private Hashtable<String, Object> finished = new Hashtable<String, Object>();
    @NotNull
    @Column(name = "backlog", columnDefinition = "json")
    @Type(JsonType.class)
    private Hashtable<String, Object> backlog = new Hashtable<String, Object>();
    @NotNull
    @Column(name = "dropped", columnDefinition = "json")
    @Type(JsonType.class)
    private Hashtable<String, Object> dropped = new Hashtable<String, Object>();

    @NotNull
    @ColumnDefault("false")
    private boolean preMigration = false;

    @CreatedDate
    private Date joinedAt;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, boolean preMigration) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.preMigration = preMigration;

    }

}
