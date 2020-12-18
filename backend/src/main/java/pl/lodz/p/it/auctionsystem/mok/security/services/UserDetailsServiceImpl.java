package pl.lodz.p.it.auctionsystem.mok.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepositoryMok;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepositoryMok userRepositoryMok;

    private MessageService messageService;

    @Autowired
    public void setUserRepository(UserRepositoryMok userRepositoryMok, MessageService messageService) {
        this.userRepositoryMok = userRepositoryMok;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String message = messageService.getMessage("exception.userNotFound");

        User user = userRepositoryMok.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(message + ": " + username));

        return UserDetailsImpl.build(user);
    }
}