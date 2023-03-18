package bibliomar.bibliomarserver.services;

import bibliomar.bibliomarserver.models.user.User;

public interface TokenService {
    String generateToken(User user);
    User validateToken(String token);
}
