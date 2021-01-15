package pl.lodz.p.it.auctionsystem.mok.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepositoryMok;
import pl.lodz.p.it.auctionsystem.mok.utils.MailService;
import pl.lodz.p.it.auctionsystem.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.utils.MessageService;
import pl.lodz.p.it.auctionsystem.utils.SortDirection;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.containsTextInName;
import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.isActive;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationException.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryMok userRepositoryMok;

    private final AccessLevelRepository accessLevelRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Value("${password_reset_code.valid_time}")
    private Long passwordResetCodeValidTime;

    @Value("${page.size}")
    private int pageSize;

    @Override
    @PreAuthorize("permitAll()")
    public Long registerUser(SignupDto signupDto) throws ApplicationException {
        User user = createUser(new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail(), signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getPhoneNumber(), LocalDateTime.now()));

        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));

        String clientAccessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
        AccessLevel clientAccessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.CLIENT).orElseThrow(() -> new EntityNotFoundException(clientAccessLevelNotFoundMessage));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, clientAccessLevel);

        user.getUserAccessLevels().add(userAccessLevel);

        mailService.sendAccountActivationMail(user);

        return userRepositoryMok.save(user).getId();
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Long addUser(UserAddDto userAddDto) throws ApplicationException {
        User user = createUser(new User(userAddDto.getUsername(), userAddDto.getPassword(),
                userAddDto.getEmail(), userAddDto.getFirstName(), userAddDto.getLastName(),
                userAddDto.getPhoneNumber(), LocalDateTime.now()));

        user.setActivated(true);

        for (Long accessLevelId : userAddDto.getAccessLevelIds()) {
            String accessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
            AccessLevel accessLevel =
                    accessLevelRepository.findById(accessLevelId).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));
            UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);

            user.getUserAccessLevels().add(userAccessLevel);
        }

        return userRepositoryMok.save(user).getId();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public UserDto getUserByUsername(String username) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMok.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Page<BasicUserDto> getUsers(UserCriteria userCriteria) {
        Pageable pageable;
        Page<User> userPage;

        if (userCriteria.getSortField() != null && userCriteria.getOrder() != null)
            pageable = PageRequest.of(userCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(userCriteria.getOrder()), userCriteria.getSortField()));
        else
            pageable = PageRequest.of(userCriteria.getPage(), pageSize);

        if (userCriteria.getQuery() == null && userCriteria.getStatus() == null)
            userPage = userRepositoryMok.findAll(pageable);
        else if (userCriteria.getQuery() != null && userCriteria.getStatus() == null)
            userPage = userRepositoryMok.findAll(containsTextInName(userCriteria.getQuery()), pageable);
        else if (userCriteria.getQuery() == null) {
            userPage = userRepositoryMok.findAll(isActive(userCriteria.getStatus()), pageable);
        } else
            userPage =
                    userRepositoryMok.findAll(containsTextInName(userCriteria.getQuery()).and(isActive(userCriteria.getStatus())), pageable);

        return userPage.map(user -> {
            BasicUserDto basicUserDto = modelMapper.map(user, BasicUserDto.class);

            basicUserDto.setUserAccessLevelNames(user.getUserAccessLevels().stream()
                    .map(userAccessLevel -> userAccessLevel.getAccessLevel().getName().toString())
                    .collect(Collectors.toList()));

            basicUserDto.getUserAccessLevelNames().sort((Comparator.comparingInt(o -> AccessLevelEnum.valueOf((String) o).ordinal())).reversed());

            return basicUserDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserDto getUserById(Long userId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");

        User user =
                userRepositoryMok.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        userDto.setAccessLevelIds(user.getUserAccessLevels().stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getId())
                .collect(Collectors.toList()));

        return userDto;
    }

    @Override
    @PreAuthorize("permitAll()")
    public UserDto getUserByPasswordResetCode(String passwordResetCode) throws ApplicationException {
        String passwordResetCodeInvalidMessage = messageService.getMessage("exception.passwordResetCodeInvalid");
        User user =
                userRepositoryMok.findByPasswordResetCode(passwordResetCode).orElseThrow(() -> new
                        InvalidParameterException(passwordResetCodeInvalidMessage));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @PreAuthorize("permitAll()")
    public boolean existsByUsername(String username) {
        return userRepositoryMok.existsByUsernameIgnoreCase(username);
    }

    @Override
    @PreAuthorize("permitAll()")
    public boolean existsByEmail(String email) {
        return userRepositoryMok.existsByEmailIgnoreCase(email);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void activateUser(String activationCode) throws ApplicationException {
        String activationCodeInvalidMessage = messageService.getMessage("exception.activationCodeInvalid");
        User user =
                userRepositoryMok.findByActivationCode(activationCode).orElseThrow(() -> new InvalidParameterException(activationCodeInvalidMessage));

        user.setActivated(true);
        user.setActivationCode(null);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void sendPasswordResetEmail(PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.emailInvalid");
        User user =
                userRepositoryMok.findByEmailIgnoreCase(passwordResetEmailDto.getEmail()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordResetCode = UUID.randomUUID().toString().replace("-", "");

        user.setPasswordResetCode(passwordResetCode);
        user.setPasswordResetCodeAddDate(LocalDateTime.now());

        mailService.sendPasswordResetEmail(user);
    }

    @Override
    @PreAuthorize("permitAll()")
    public void resetPassword(String passwordResetCode, PasswordResetDto passwordResetDto, String ifMatch) throws ApplicationException {
        String passwordResetCodeInvalidMessage = messageService.getMessage("exception.passwordResetCodeInvalid");
        User user =
                userRepositoryMok.findByPasswordResetCode(passwordResetCode).orElseThrow(() -> new
                        InvalidParameterException(passwordResetCodeInvalidMessage));
        LocalDateTime passwordResetCodeAddDate = user.getPasswordResetCodeAddDate();
        LocalDateTime passwordResetCodeValidityDate = passwordResetCodeAddDate.plusMinutes(passwordResetCodeValidTime);

        if (LocalDateTime.now().isAfter(passwordResetCodeValidityDate)) {
            String passwordResetCodeExpiredMessage = messageService.getMessage("exception.passwordResetCodeExpired");

            throw new PasswordResetCodeExpiredException(passwordResetCodeExpiredMessage);
        }

        String passwordHash = passwordEncoder.encode(passwordResetDto.getNewPassword());

        User userCopy = new User(Long.parseLong(ifMatch.replace("\"", "")), user.getBusinessKey(), user.getId(),
                user.getUsername(), passwordHash, user.getEmail(), user.isActivated(), user.getCreatedAt(),
                user.getActivationCode(), null, null,
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber());

        userRepositoryMok.saveAndFlush(userCopy);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void updateDetailsByUsername(String username, OwnAccountDetailsUpdateDto ownAccountDetailsUpdateDto,
                                        String ifMatch) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMok.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        User userCopy = new User(Long.parseLong(ifMatch.replace("\"", "")), user.getBusinessKey(), user.getId(),
                user.getUsername(), user.getPassword(), user.getEmail(), user.isActivated(), user.getCreatedAt(),
                user.getActivationCode(), user.getPasswordResetCode(), user.getPasswordResetCodeAddDate(),
                ownAccountDetailsUpdateDto.getFirstName(), ownAccountDetailsUpdateDto.getLastName(),
                ownAccountDetailsUpdateDto.getPhoneNumber());

        userRepositoryMok.saveAndFlush(userCopy);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void updateUserDetailsById(Long userId, UserAccountDetailsUpdateDto userAccountDetailsUpdateDto,
                                      String ifMatch) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMok.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        User userCopy = new User(Long.parseLong(ifMatch.replace("\"", "")), user.getBusinessKey(), user.getId(),
                user.getUsername(), user.getPassword(), user.getEmail(), user.isActivated(), user.getCreatedAt(),
                user.getActivationCode(), user.getPasswordResetCode(), user.getPasswordResetCodeAddDate(),
                userAccountDetailsUpdateDto.getFirstName(), userAccountDetailsUpdateDto.getLastName(),
                userAccountDetailsUpdateDto.getPhoneNumber());

        userRepositoryMok.saveAndFlush(userCopy);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','MANAGER','CLIENT')")
    public void changePasswordByUsername(String username, OwnPasswordChangeDto ownPasswordChangeDto, String ifMatch) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMok.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));

        if (!passwordEncoder.matches(ownPasswordChangeDto.getCurrentPassword(), user.getPassword())) {
            String passwordIncorrectMessage = messageService.getMessage("exception.passwordIncorrect");

            throw new IncorrectPasswordException(passwordIncorrectMessage);
        }

        if (passwordEncoder.matches(ownPasswordChangeDto.getNewPassword(), user.getPassword())) {
            String passwordIdenticalMessage = messageService.getMessage("exception.passwordIdentical");

            throw new PasswordIdenticalException(passwordIdenticalMessage);
        }

        String passwordHash = passwordEncoder.encode(ownPasswordChangeDto.getNewPassword());

        User userCopy = new User(Long.parseLong(ifMatch.replace("\"", "")), user.getBusinessKey(), user.getId(),
                user.getUsername(), passwordHash, user.getEmail(), user.isActivated(), user.getCreatedAt(),
                user.getActivationCode(), user.getPasswordResetCode(), user.getPasswordResetCodeAddDate(),
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber());

        userRepositoryMok.saveAndFlush(userCopy);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void changePasswordById(Long userId, UserPasswordChangeDto userPasswordChangeDto, String ifMatch) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMok.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String passwordHash = passwordEncoder.encode(userPasswordChangeDto.getNewPassword());

        User userCopy = new User(Long.parseLong(ifMatch.replace("\"", "")), user.getBusinessKey(), user.getId(),
                user.getUsername(), passwordHash, user.getEmail(), user.isActivated(), user.getCreatedAt(),
                user.getActivationCode(), user.getPasswordResetCode(), user.getPasswordResetCodeAddDate(),
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber());

        userRepositoryMok.saveAndFlush(userCopy);
    }

    private User createUser(User user) throws ApplicationException {
        if (userRepositoryMok.existsByUsernameIgnoreCase(user.getUsername())) {
            String usernameNotUniqueMessage = messageService.getMessage("exception.usernameNotUnique");

            throw new ValueNotUniqueException(usernameNotUniqueMessage);
        }

        if (userRepositoryMok.existsByEmailIgnoreCase(user.getEmail())) {
            String emailNotUnique = messageService.getMessage("exception.emailNotUnique");

            throw new ValueNotUniqueException(emailNotUnique);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }
}