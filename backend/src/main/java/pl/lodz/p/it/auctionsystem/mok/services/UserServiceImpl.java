package pl.lodz.p.it.auctionsystem.mok.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("permitAll()")
    public Long registerUser(SignupDto signupDto) throws ApplicationException {
        User user = createUser(new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail(), signupDto.getFirstName(), signupDto.getLastName(),
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

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Long addUser(UserAddDto userAddDto) throws ApplicationException {
        User user = createUser(new User(userAddDto.getUsername(), userAddDto.getPassword(),
                userAddDto.getEmail(), userAddDto.getFirstName(), userAddDto.getLastName(),
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
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public OwnAccountDetailsDto getUserByUsername(String username) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        return modelMapper.map(user, OwnAccountDetailsDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<UserDto> searchUsers(UserCriteria userCriteria) {
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

            return userDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserAccountDetailsDto getUserById(Long userId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        UserAccountDetailsDto userAccountDetailsDto = modelMapper.map(user, UserAccountDetailsDto.class);

        userAccountDetailsDto.setAccessLevelIds(user.getUserAccessLevels().stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getId())
                .collect(Collectors.toList()));

        return userAccountDetailsDto;
    }

    @Override
    @PreAuthorize("permitAll()")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @PreAuthorize("permitAll()")
    public boolean existsByEmail(String email) {
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
    @PreAuthorize("permitAll()")
    public void sendPasswordResetEmail(PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.emailInvalid");
        User user =
                userRepository.findByEmailIgnoreCase(passwordResetEmailDto.getEmail()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordResetCode = UUID.randomUUID().toString().replace("-", "");

        user.setPasswordResetCode(passwordResetCode);
        user.setPasswordResetCodeAddDate(LocalDateTime.now());

        mailService.sendPasswordResetEmail(user);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void resetPassword(String passwordResetCode, PasswordResetDto passwordResetDto) throws ApplicationException {
        String passwordResetCodeInvalidMessage = messageService.getMessage("exception.passwordResetCodeInvalid");
        User user =
                userRepository.findByPasswordResetCode(passwordResetCode).orElseThrow(() -> new
                        InvalidParameterException(passwordResetCodeInvalidMessage));
        LocalDateTime passwordResetCodeAddDate = user.getPasswordResetCodeAddDate();
        LocalDateTime passwordResetCodeValidityDate = passwordResetCodeAddDate.plusMinutes(passwordResetCodeValidTime);

        if (LocalDateTime.now().isAfter(passwordResetCodeValidityDate)) {
            String passwordResetCodeExpiredMessage = messageService.getMessage("exception.passwordResetCodeExpired");

            throw new PasswordResetCodeExpiredException(passwordResetCodeExpiredMessage);
        }

        String passwordHash = passwordEncoder.encode(passwordResetDto.getNewPassword());

        user.setPassword(passwordHash);
        user.setPasswordResetCode(null);
        user.setPasswordResetCodeAddDate(null);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void updateDetailsByUsername(String username, OwnAccountDetailsUpdateDto ownAccountDetailsUpdateDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        user.setFirstName(ownAccountDetailsUpdateDto.getFirstName());
        user.setLastName(ownAccountDetailsUpdateDto.getLastName());
        user.setPhoneNumber(ownAccountDetailsUpdateDto.getPhoneNumber());
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void updateUserDetailsById(Long userId, UserAccountDetailsUpdateDto userAccountDetailsUpdateDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        user.setFirstName(userAccountDetailsUpdateDto.getFirstName());
        user.setLastName(userAccountDetailsUpdateDto.getLastName());
        user.setPhoneNumber(userAccountDetailsUpdateDto.getPhoneNumber());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void changePasswordByUsername(String username, OwnPasswordChangeDto ownPasswordChangeDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        if (!passwordEncoder.matches(ownPasswordChangeDto.getCurrentPassword(), user.getPassword())) {
            String passwordIncorrectMessage = messageService.getMessage("exception.passwordIncorrect");

            throw new IncorrectPasswordException(passwordIncorrectMessage);
        }

        if (passwordEncoder.matches(ownPasswordChangeDto.getNewPassword(), user.getPassword())) {
            String passwordIdenticalMessage = messageService.getMessage("exception.passwordIdentical");

            throw new PasswordIdenticalException(passwordIdenticalMessage);
        }

        String passwordHash = passwordEncoder.encode(ownPasswordChangeDto.getNewPassword());

        user.setPassword(passwordHash);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void changePasswordById(Long userId, UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordHash = passwordEncoder.encode(userPasswordChangeDto.getNewPassword());

        user.setPassword(passwordHash);
    }

    private User createUser(User user) throws ApplicationException {
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
}