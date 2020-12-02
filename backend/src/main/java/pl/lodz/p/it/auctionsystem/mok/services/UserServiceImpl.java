package pl.lodz.p.it.auctionsystem.mok.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.mok.utils.SortDirection;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final ModelMapper modelMapper;

    @Value("${password_reset_code.valid_time}")
    private Long passwordResetCodeValidTime;

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AccessLevelRepository accessLevelRepository,
                           PasswordEncoder passwordEncoder, MailService mailService, MessageService messageService,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Long createUser(UserAddDto userAddDto) throws ApplicationException {
        User user = addUser(new User(userAddDto.getUsername(), userAddDto.getPassword(),
                userAddDto.getEmail().toLowerCase(), userAddDto.getFirstName(), userAddDto.getLastName(),
                userAddDto.getPhoneNumber()));
        user.setActivated(true);

        for (Long accessLevelId : userAddDto.getAccessLevelIds()) {
            String accessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
            AccessLevel accessLevel =
                    accessLevelRepository.findById(accessLevelId).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));
            UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);

            user.getUserAccessLevels().add(userAccessLevel);
        }

        return userRepository.save(user).getId();
    }

    @Override
    @PreAuthorize("permitAll()")
    public Long registerUser(SignupDto signupDto) throws ApplicationException {
        User user = addUser(new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail().toLowerCase(), signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getPhoneNumber()));
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));

        String clientAccessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
        AccessLevel clientAccessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT).orElseThrow(() -> new EntityNotFoundException(clientAccessLevelNotFoundMessage));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, clientAccessLevel);

        user.getUserAccessLevels().add(userAccessLevel);

        mailService.sendAccountActivationMail(user);

        return userRepository.save(user).getId();
    }

    private User addUser(User user) throws ApplicationException {
        if (userRepository.existsByUsername(user.getUsername())) {
            String usernameNotUniqueMessage = messageService.getMessage("exception.usernameNotUnique");

            throw new ValueNotUniqueException(usernameNotUniqueMessage);
        }

        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            String emailNotUnique = messageService.getMessage("exception.emailNotUnique");

            throw new ValueNotUniqueException(emailNotUnique);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<UserDto> searchUsers(@Valid UserCriteria userCriteria) {
        Pageable pageable;
        Page<User> userPage;

        if (userCriteria.getSortField() != null && userCriteria.getOrder() != null)
            pageable = PageRequest.of(userCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(userCriteria.getOrder()), userCriteria.getSortField()));
        else
            pageable = PageRequest.of(userCriteria.getPage(), pageSize);

        if (userCriteria.getQuery() == null && userCriteria.getStatus() == null)
            userPage = userRepository.findAll(pageable);
        else if (userCriteria.getQuery() != null && userCriteria.getStatus() == null)
            userPage = userRepository.findAll(containsTextInName(userCriteria.getQuery()), pageable);
        else if (userCriteria.getQuery() == null) {
            userPage = userRepository.findAll(isActive(userCriteria.getStatus()), pageable);
        } else
            userPage =
                    userRepository.findAll(containsTextInName(userCriteria.getQuery()).and(isActive(userCriteria.getStatus())), pageable);

        return userPage.map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            userDto.setUserAccessLevelNames(user.getUserAccessLevels().stream()
                    .map(userAccessLevel -> userAccessLevel.getAccessLevel().getName().toString())
                    .collect(Collectors.toList()));

            userDto.getUserAccessLevelNames().sort((Comparator.comparingInt(o -> AccessLevelEnum.valueOf((String) o).ordinal())).reversed());

            return modelMapper.map(user, UserDto.class);
        });
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserDetailsDto getUserById(Long userId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        UserDetailsDto userDetailsDto = modelMapper.map(user, UserDetailsDto.class);

        userDetailsDto.setAccessLevelIds(user.getUserAccessLevels().stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getId())
                .collect(Collectors.toList()));

        return userDetailsDto;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public OwnDetailsDto getUserByUsername(String username) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        return modelMapper.map(user, OwnDetailsDto.class);
    }

    @Override
    @PreAuthorize("permitAll()")
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @PreAuthorize("permitAll()")
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void activateUser(String activationCode) throws ApplicationException {
        String activationCodeInvalidMessage = messageService.getMessage("exception.activationCodeInvalid");
        User user =
                userRepository.findByActivationCode(activationCode).orElseThrow(() -> new InvalidParameterException(activationCodeInvalidMessage));

        user.setActivated(true);
        user.setActivationCode(null);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void updateUserDetailsByUserId(Long userId, User user) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        userFromRepository.setFirstName(user.getFirstName());
        userFromRepository.setLastName(user.getLastName());
        userFromRepository.setPhoneNumber(user.getPhoneNumber());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void updateDetailsByUsername(OwnDetailsUpdateDto ownDetailsUpdateDto, String username) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        user.setFirstName(ownDetailsUpdateDto.getFirstName());
        user.setLastName(ownDetailsUpdateDto.getLastName());
        user.setPhoneNumber(ownDetailsUpdateDto.getPhoneNumber());
    }

    @Override
    @PreAuthorize("permitAll()")
    public void sendPasswordResetEmail(PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.emailInvalid");
        User userFromRepository =
                userRepository.findByEmailIgnoreCase(passwordResetEmailDto.getEmail()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordResetCode = UUID.randomUUID().toString().replace("-", "");

        userFromRepository.setPasswordResetCode(passwordResetCode);
        userFromRepository.setPasswordResetCodeAddDate(LocalDateTime.now());

        mailService.sendPasswordResetEmail(userFromRepository);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void resetPassword(String passwordResetCode, PasswordResetDto passwordResetDto) throws ApplicationException {
        String passwordResetCodeInvalidMessage = messageService.getMessage("exception.passwordResetCodeInvalid");
        User userFromRepository =
                userRepository.findByPasswordResetCode(passwordResetCode).orElseThrow(() -> new
                        InvalidParameterException(passwordResetCodeInvalidMessage));
        LocalDateTime passwordResetCodeAddDate = userFromRepository.getPasswordResetCodeAddDate();
        LocalDateTime passwordResetCodeValidityDate = passwordResetCodeAddDate.plusMinutes(passwordResetCodeValidTime);

        if (LocalDateTime.now().isAfter(passwordResetCodeValidityDate)) {
            String passwordResetCodeExpiredMessage = messageService.getMessage("exception.passwordResetCodeExpired");

            throw new PasswordResetCodeExpiredException(passwordResetCodeExpiredMessage);
        }

        String passwordHash = passwordEncoder.encode(passwordResetDto.getNewPassword());

        userFromRepository.setPassword(passwordHash);
        userFromRepository.setPasswordResetCode(null);
        userFromRepository.setPasswordResetCodeAddDate(null);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void changePassword(String newPassword, String currentPassword, Authentication authentication) throws ApplicationException {
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User userFromRepository =
                userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        if (!passwordEncoder.matches(currentPassword, userFromRepository.getPassword())) {
            String passwordIncorrectMessage = messageService.getMessage("exception.passwordIncorrect");

            throw new IncorrectPasswordException(passwordIncorrectMessage);
        }

        if (passwordEncoder.matches(newPassword, userFromRepository.getPassword())) {
            String passwordIdenticalMessage = messageService.getMessage("exception.passwordIdentical");

            throw new PasswordIdenticalException(passwordIdenticalMessage);
        }

        String passwordHash = passwordEncoder.encode(newPassword);

        userFromRepository.setPassword(passwordHash);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void changePassword(Long userId, String newPassword) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User userFromRepository =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordHash = passwordEncoder.encode(newPassword);

        userFromRepository.setPassword(passwordHash);
    }
}