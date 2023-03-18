package bibliomar.bibliomarserver.models;

import bibliomar.bibliomarserver.models.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Instant issuedAt = Instant.now();

    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant expirationTime = Instant.now().plusMillis(EXPIRATION_TIME);

    // Setting this to true implies this token was forcefully invalidated
    private boolean forceExpired = false;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public boolean isExpired() {
        return forceExpired || Instant.now().isAfter(expirationTime);
    }

    public void expire() {
        forceExpired = true;
    }


}
