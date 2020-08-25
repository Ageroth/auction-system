package pl.lodz.p.it.auctionsystem.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.auctionsystem.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
    
    private final Long id;
    
    private final String username;
    
    @JsonIgnore
    private final String password;
    
    private final String email;
    
    private final boolean enabled;
    
    private final Collection<? extends GrantedAuthority> authorities;
    
    private final String firstName;
    
    private final String lastName;
    
    private final String phoneNumber;
    
    public UserDetailsImpl(Long id, String username, String password, String email, boolean activated,
                           Collection<? extends GrantedAuthority> authorities, String firstName, String lastName,
                           String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = activated;
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getUserAccessLevels().stream()
                .map(userAccessLevel -> new SimpleGrantedAuthority(userAccessLevel.getAccessLevel().getName().name()))
                .collect(Collectors.toList());
        
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.isActivated(),
                authorities,
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber());
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
        return true;
    }
}