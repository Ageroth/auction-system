package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = ApplicationException.class)
public class UserService implements IUserService {
    
    private final UserRepository userRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${CLIENT_ACCESS_LEVEL}")
    private String CLIENT_ACCESS_LEVEL;
    
    
    @Autowired
    public UserService(UserRepository userRepository, AccessLevelRepository accessLevelRepository,
                       PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }
    
    @Override
    public void createUser(User user) throws ApplicationException {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user.setActivated(true);
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
        
        UserAccessLevel userAccessLevel = new UserAccessLevel(user,
                accessLevelRepository.findAccessLevelByName(CLIENT_ACCESS_LEVEL));
        
        user.getUserAccessLevels().add(userAccessLevel);
        
        userRepository.save(user);
    }
    
    @Override
    public void registerUser(User user) throws ApplicationException {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        
        user.setPassword(passwordHash);
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
        
        UserAccessLevel userAccessLevel = new UserAccessLevel(user,
                accessLevelRepository.findAccessLevelByName(CLIENT_ACCESS_LEVEL));
        
        user.getUserAccessLevels().add(userAccessLevel);
        
        userRepository.save(user);
        
        mailService.sendVerificationMail(user);
    }
    
    @Override
    public List<User> getAllUsers() {
        return null;
    }
    
    @Override
    public Optional<User> getUserById(Long userId) throws ApplicationException {
//        TODO: Throw an exception in case user is not found
        return userRepository.findById(userId);
    }
    
    @Override
    public Optional<User> getUserByLogin(String login) throws ApplicationException {
//        TODO: Throw an exception in case user is not found
        return userRepository.findUserByLogin(login);
    }
    
    @Override
    public void activateUser(String activationCode) throws ApplicationException {
        //        TODO: Throw an exception in case user is not found
        Optional<User> user = userRepository.findUserByActivationCode(activationCode);
        
        user.get().setActivated(true);
        user.get().setActivationCode(null);
    }
    
    @Override
    public void updateUserDetailsByUserId(Long userId, User user) throws ApplicationException {
        Optional<User> userFromRepository = getUserById(userId);
        
        updateUserDetails(userFromRepository.get(), user);
    }
    
    @Override
    public void updateUserDetailsByUserLogin(String userLogin, User user) throws ApplicationException {
        Optional<User> userFromRepository = getUserByLogin(userLogin);
        
        updateUserDetails(userFromRepository.get(), user);
    }
    
    private void updateUserDetails(User userFromRepository, User user) {
        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setPhoneNumber(user.getPhoneNumber());
    }
}