package bibliomar.bibliomarserver.repositories;

import bibliomar.bibliomarserver.models.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
    Token findByToken(String token);

}
