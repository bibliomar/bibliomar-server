package bibliomar.bibliomarserver.service.user;

import bibliomar.bibliomarserver.model.user.User;
import bibliomar.bibliomarserver.model.user.UserDetailsImpl;
import bibliomar.bibliomarserver.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (user.isPreMigration()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User should ask for password reset");
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return userDetails;
    }
}
