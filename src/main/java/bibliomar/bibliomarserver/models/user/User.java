package bibliomar.bibliomarserver.models.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import bibliomar.bibliomarserver.models.library.UserLibrary;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String username;
    @NotNull
    @JsonIgnore
    private String password;
    @Email
    @NotNull
    private String email;

    // Do *not* use FetchType.LAZY if you want to return this model as JSON.
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @JsonIgnoreProperties("username")
    private UserLibrary userLibrary;

    @NotNull
    @ColumnDefault("false")
    private boolean preMigration = false;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @NotNull
    @JsonProperty("role")
    private UserRole role = UserRole.ROLE_USER;

    private boolean privateProfile = false;

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
