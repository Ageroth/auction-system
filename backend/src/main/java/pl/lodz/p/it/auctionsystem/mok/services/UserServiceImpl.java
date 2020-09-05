package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.containsTextInName;
import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.isActive;

@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = ApplicationException.class)
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    private final AccessLevelRepository accessLevelRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final MailService mailService;
    
    @Value("${CLIENT_ACCESS_LEVEL}")
    private String CLIENT_ACCESS_LEVEL;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, AccessLevelRepository accessLevelRepository,
                           PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }
    
    @Override
    public User createUser(User user) throws ApplicationException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivated(true);
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
    
        UserAccessLevel userAccessLevel = new UserAccessLevel(user,
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT));
    
        user.getUserAccessLevels().add(userAccessLevel);
        return userRepository.save(user);
    }
    
    @Override
    public User registerUser(User user) throws ApplicationException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
        
        UserAccessLevel userAccessLevel = new UserAccessLevel(user,
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT));
        
        user.getUserAccessLevels().add(userAccessLevel);
    
        mailService.sendAccountVerificationMail(user);
    
        return userRepository.save(user);
    }
    
    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    public Page<User> getFilteredUsers(String query, boolean status, Pageable pageable) {
        return userRepository.findAll(containsTextInName(query).and(isActive(status)), pageable);
    }
    
    public Page<User> getFilteredUsers(String query, Pageable pageable) {
        return userRepository.findAll(containsTextInName(query), pageable);
    }
    
    public Page<User> getFilteredUsers(boolean status, Pageable pageable) {
        return userRepository.findAll(isActive(status), pageable);
    }
    
    @Override
    public Optional<User> getUserById(Long userId) throws ApplicationException {
//        TODO: Throw an exception in case user is not found
        return userRepository.findById(userId);
    }
    
    @Override
    public Optional<User> getUserByUsername(String username) throws ApplicationException {
//        TODO: Throw an exception in case user is not found
        return userRepository.findByUsername(username);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public void activateUser(String activationCode) throws ApplicationException {
        //        TODO: Throw an exception in case user is not found
        Optional<User> user = userRepository.findByActivationCode(activationCode);
        
        user.get().setActivated(true);
        user.get().setActivationCode(null);
    }
    
    @Override
    public void updateUserDetails(Long userId, User user) {
        Optional<User> userFromRepository = userRepository.findById(userId);
        
        userFromRepository.get().setFirstName(user.getFirstName());
        userFromRepository.get().setLastName(user.getLastName());
        userFromRepository.get().setPhoneNumber(user.getPhoneNumber());
    }
    
    @Override
    public void sendResetPasswordMail(User user) throws ApplicationException {
        Optional<User> userFromRepository = userRepository.findByUsername(user.getUsername());
        String resetPasswordCode = UUID.randomUUID().toString().replace("-", "");
        
        userFromRepository.get().setResetPasswordCode(resetPasswordCode);
        userFromRepository.get().setResetPasswordCodeAddDate(LocalDateTime.now());
        mailService.sendResetPasswordMail(user);
    }
    
    @Override
    public void resetPassword(User user) throws ApplicationException {
        Optional<User> userFromRepository = userRepository.findByResetPasswordCode(user.getResetPasswordCode());
        LocalDateTime resetPasswordCodeAddDate = userFromRepository.get().getResetPasswordCodeAddDate();
        String passwordHash = passwordEncoder.encode(user.getPassword());
        
        userFromRepository.get().setPassword(passwordHash);
    }
    
    @Override
    public void changePassword(Long userId, String newPassword, String oldPassword) throws ApplicationException {
        Optional<User> userFromRepository = userRepository.findById(userId);
        String passwordHash = passwordEncoder.encode(newPassword);
        
        userFromRepository.get().setPassword(passwordHash);
    }
    
    @Override
    public void changePassword(Long userId, String newPassword) throws ApplicationException {
        Optional<User> userFromRepository = userRepository.findById(userId);
        String passwordHash = passwordEncoder.encode(newPassword);
        
        userFromRepository.get().setPassword(passwordHash);
    }
}