package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.containsTextInName;
import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.isActive;

@Service
@Transactional(rollbackFor = ApplicationException.class)
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    private final AccessLevelRepository accessLevelRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final MailService mailService;
    
    private final MessageService messageService;
    
    @Value("${password.reset.code.valid.time}")
    private Long passwordResetCodeValidTime;
    
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
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User createUser(User user) throws ApplicationException {
        User addedUser = addUser(user);
        addedUser.setActivated(true);
    
        return userRepository.save(addedUser);
    }
    
    @Override
    @PreAuthorize("permitAll()")
    public User registerUser(User user) throws ApplicationException {
        User addedUser = addUser(user);
        addedUser.setActivationCode(UUID.randomUUID().toString().replace("-", ""));
        
        mailService.sendAccountActivationMail(addedUser);
        
        return userRepository.save(addedUser);
    }
    
    private User addUser(User user) throws ApplicationException {
        if (userRepository.existsByUsername(user.getUsername())) {
            String usernameNotUniqueMessage = messageService.getMessage("exception.usernameNotUnique");
            
            throw new ValueNotUniqueException(usernameNotUniqueMessage);
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            String emailNotUnique = messageService.getMessage("exception.emailNotUnique");
            
            throw new ValueNotUniqueException(emailNotUnique);
        }
        
        String clientAccessLevelNotFoundMessage = messageService.getMessage("accessLevelNotFound");
        AccessLevel clientAccessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT).orElseThrow(() -> new EntityNotFoundException(clientAccessLevelNotFoundMessage));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, clientAccessLevel);
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getUserAccessLevels().add(userAccessLevel);
        
        return user;
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<User> getFilteredUsers(String query, boolean status, Pageable pageable) {
        return userRepository.findAll(containsTextInName(query).and(isActive(status)), pageable);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<User> getFilteredUsers(String query, Pageable pageable) {
        return userRepository.findAll(containsTextInName(query), pageable);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<User> getFilteredUsers(boolean status, Pageable pageable) {
        return userRepository.findAll(isActive(status), pageable);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User getUserById(Long userId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
    }
    
    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public User getCurrentUser(Authentication authentication) throws ApplicationException {
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
    }
    
    @Override
    @PreAuthorize("permitAll()")
    public void activateUser(String activationCode) throws ApplicationException {
        String activationCodeInvalidMessage = messageService.getMessage("activationCodeInvalid");
        User user =
                userRepository.findByActivationCode(activationCode).orElseThrow(() -> new InvalidParameterException(activationCodeInvalidMessage));
        
        user.setActivated(true);
        user.setActivationCode(null);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void updateUserDetailsByUserId(Long userId, User user) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        
        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setPhoneNumber(user.getPhoneNumber());
    }
    
    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void updateCurrentUserDetails(User user, Authentication authentication) throws ApplicationException {
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        
        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setPhoneNumber(user.getPhoneNumber());
    }
    
    @Override
    @PreAuthorize("permitAll()")
    public void sendPasswordResetMail(String email) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordResetCode = UUID.randomUUID().toString().replace("-", "");
        
        userFromRepository.setPasswordResetCode(passwordResetCode);
        userFromRepository.setPasswordResetCodeAddDate(LocalDateTime.now());
        mailService.sendPasswordResetMail(userFromRepository);
    }
    
    @Override
    @PreAuthorize("permitAll()")
    public void resetPassword(String passwordResetCode, String newPassword) throws ApplicationException {
        String passwordResetCodeInvalidMessage = messageService.getMessage("passwordResetCodeInvalid");
        User userFromRepository =
                userRepository.findByPasswordResetCode(passwordResetCode).orElseThrow(() -> new
                        InvalidParameterException(passwordResetCodeInvalidMessage));
        LocalDateTime passwordResetCodeAddDate = userFromRepository.getPasswordResetCodeAddDate();
        LocalDateTime passwordResetCodeValidityDate = passwordResetCodeAddDate.plusMinutes(passwordResetCodeValidTime);
        
        if (LocalDateTime.now().isAfter(passwordResetCodeValidityDate)) {
            String passwordResetCodeExpiredMessage = messageService.getMessage("passwordResetCodeExpired");
            
            throw new PasswordResetCodeExpiredException(passwordResetCodeExpiredMessage);
        }
        
        String passwordHash = passwordEncoder.encode(newPassword);
        
        userFromRepository.setPassword(passwordHash);
        userFromRepository.setPasswordResetCode(null);
        userFromRepository.setPasswordResetCodeAddDate(null);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void changePassword(String newPassword, String oldPassword, Authentication authentication) throws ApplicationException {
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        
        if (!passwordEncoder.matches(oldPassword, userFromRepository.getPassword())) {
            String passwordIncorrectMessage = messageService.getMessage("passwordIncorrect");
            
            throw new IncorrectPasswordException(passwordIncorrectMessage);
        }
        
        String passwordHash = passwordEncoder.encode(newPassword);
        
        userFromRepository.setPassword(passwordHash);
    }
    
    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void changePassword(Long userId, String newPassword) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordHash = passwordEncoder.encode(newPassword);
        
        userFromRepository.setPassword(passwordHash);
    }
}