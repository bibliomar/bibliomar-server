package bibliomar.bibliomarserver.services.impl;

import java.security.SecureRandom;
import java.util.Base64;

import bibliomar.bibliomarserver.models.Token;
import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.repositories.TokenRepository;
import bibliomar.bibliomarserver.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String generateToken(User user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getEncoder().encodeToString(randomBytes);
        tokenRepository.save(new Token(token, user));

        return token;
    }

    /**
     * Attempts to validate a token
     * @param token the token to validate
     * @throws ResponseStatusException if the token is invalid
     * @returns the user associated with the token if validation succeeds
     */
    @Override
    public User validateToken(String token) {
        Token tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token doesn't exist");
        } else if (tokenEntity.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is expired");
        }
        /**
         * For performance reasons,
         */
        return tokenEntity.getUser();

    }
}
