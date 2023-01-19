package bibliomar.bibliomarserver.service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEncoderService {
    /*
     * It's adviced to use the PasswordEncoder class, as it instantiates this.
     * @Autowired
     * PasswordEncoder passwordEncoder;
     * It's defined in the SecurityConfig class.
     */
    private static final SecretKeyFactoryAlgorithm PBKDF2_ALGORITHM = SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512;
    private static final int SALT_BYTE_SIZE = 16;
    private static final int ROUNDS = 29000;
    @Value("${password.secret}")
    String secret;
    Pbkdf2PasswordEncoder encoder;

    @PostConstruct
    public void buildEncoder() {
        this.encoder = new Pbkdf2PasswordEncoder(this.secret, SALT_BYTE_SIZE, ROUNDS, PBKDF2_ALGORITHM);
    }

    // To be used in SecurityConfig class
    public Pbkdf2PasswordEncoder getEncoder() {
        return this.encoder;
    }


}
