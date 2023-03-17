package bibliomar.bibliomarserver.config;

import bibliomar.bibliomarserver.models.user.User;
import bibliomar.bibliomarserver.models.user.UserDetailsImpl;
import bibliomar.bibliomarserver.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(()-> new UsernameNotFoundException("Can't find user with this username or email: "+usernameOrEmail));
         return new UserDetailsImpl(user);
    }

    /**
     * Only call this method if you are sure the security context is not empty.
     *
     * @return UserDetails of the authenticated user
     */
    public static UserDetails getAuthenticatedUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
