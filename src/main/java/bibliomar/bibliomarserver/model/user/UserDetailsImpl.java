package bibliomar.bibliomarserver.model.user;

import bibliomar.bibliomarserver.model.library.UserLibrary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    @JsonIgnore
    User baseUser;


    public UserDetailsImpl(User baseUser) {
        this.baseUser = baseUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(baseUser.getRole().toString());
        return List.of(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return baseUser.getPassword();
    }

    @Override
    public String getUsername() {
        return baseUser.getUsername();
    }

    public String getEmail() {
        return baseUser.getEmail();
    }

    public UserLibrary getUserLibrary() {
        return baseUser.getUserLibrary();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !baseUser.isPreMigration();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
