package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.exceptions.IncorrectPasswordException;
import pl.lodz.p.it.auctionsystem.exceptions.ResetPasswordCodeExpiredException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import java.time.LocalDateTime;
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
    
    private final MessageService messageService;
    
    @Value("${CLIENT_ACCESS_LEVEL}")
    private String CLIENT_ACCESS_LEVEL;
    
    @Value("${resetPasswordCodeValidTime}")
    private Long resetPasswordCodeValidTime;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, AccessLevelRepository accessLevelRepository,
                           PasswordEncoder passwordEncoder, MailService mailService, MessageService messageService) {
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.messageService = messageService;
    }
    
    @Override
    public User createUser(User user) throws ApplicationException {
        String message = messageService.getMessage("clientAccessLevelNotFound");
        AccessLevel clientAccessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT).orElseThrow(() -> new EntityNotFoundException(message));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, clientAccessLevel);
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivated(true);
        user.getUserAccessLevels().add(userAccessLevel);
    
        return userRepository.save(user);
    }
    
    @Override
    public User registerUser(User user) throws ApplicationException {
        String message = messageService.getMessage("clientAccessLevelNotFound");
        AccessLevel clientAccessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT).orElseThrow(() -> new EntityNotFoundException(message));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, clientAccessLevel);
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
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
    public User getUserById(Long userId) throws ApplicationException {
        String message = messageService.getMessage("userNotFound");
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(message));
        
        return user;
    }
    
    @Override
    public User getUserByUsername(String username) throws ApplicationException {
        String message = messageService.getMessage("userNotFound");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(message));
        
        return user;
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
        String message = messageService.getMessage("userNotFound");
        User user =
                userRepository.findByActivationCode(activationCode).orElseThrow(() -> new EntityNotFoundException(message));
//        TODO: invalid activation code(up)
        user.setActivated(true);
        user.setActivationCode(null);
    }
    
    @Override
    public void updateUserDetails(Long userId, User user) throws ApplicationException {
        String message = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(message));
        
        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setPhoneNumber(user.getPhoneNumber());
    }
    
    @Override
    public void sendPasswordResetMail(String email) throws ApplicationException {
        String message = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(message));
        String resetPasswordCode = UUID.randomUUID().toString().replace("-", "");
    
        userFromRepository.setResetPasswordCode(resetPasswordCode);
        userFromRepository.setResetPasswordCodeAddDate(LocalDateTime.now());
        mailService.sendPasswordResetMail(userFromRepository);
    }
    
    @Override
    public void resetPassword(String resetPasswordCode, String newPassword) throws ApplicationException {
        String userMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findByResetPasswordCode(resetPasswordCode).orElseThrow(() -> new
                        EntityNotFoundException(userMessage));
        LocalDateTime resetPasswordCodeAddDate = userFromRepository.getResetPasswordCodeAddDate();
        LocalDateTime resetPasswordCodeValidityDate = resetPasswordCodeAddDate.plusMinutes(15);
//        TODO: invalid resetPasswordCode(up) exception
    
        if (LocalDateTime.now().isAfter(resetPasswordCodeValidityDate)) {
            String resetPasswordCodeMessage = messageService.getMessage("resetPasswordCodeExpired");
        
            throw new ResetPasswordCodeExpiredException(resetPasswordCodeMessage);
        }
    
        String passwordHash = passwordEncoder.encode(newPassword);
    
        userFromRepository.setPassword(passwordHash);
        userFromRepository.setResetPasswordCode(null);
        userFromRepository.setResetPasswordCodeAddDate(null);
    }
    
    @Override
    public void changePassword(Long userId, String newPassword, String oldPassword) throws ApplicationException {
        String userMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userMessage));
    
        if (!passwordEncoder.matches(oldPassword, userFromRepository.getPassword())) {
            String passwordMessage = messageService.getMessage("incorrectPassword");
            throw new IncorrectPasswordException(passwordMessage);
        }
    
        String passwordHash = passwordEncoder.encode(newPassword);
    
        userFromRepository.setPassword(passwordHash);
    }
    
    @Override
    public void changePassword(Long userId, String newPassword) throws ApplicationException {
        String message = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(message));
        String passwordHash = passwordEncoder.encode(newPassword);
    
        userFromRepository.setPassword(passwordHash);
    }
}