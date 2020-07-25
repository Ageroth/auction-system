package pl.p.lodz.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.p.lodz.auctionsystem.entities.User;
import pl.p.lodz.auctionsystem.entities.UserAccessLevel;
import pl.p.lodz.auctionsystem.exceptions.ApplicationException;
import pl.p.lodz.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.p.lodz.auctionsystem.mok.repositories.UserRepository;
import pl.p.lodz.auctionsystem.mok.utils.MailService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AccessLevelRepository accessLevelRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${CLIENT_ACCESS_LEVEL}")
    private String CLIENT_ACCESS_LEVEL;


    @Autowired
    public UserService(UserRepository userRepository, AccessLevelRepository accessLevelRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public void createUser(User user) throws ApplicationException {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user.setActivated(true);
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));

        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevelRepository.findAccessLevelByName(CLIENT_ACCESS_LEVEL));

        user.getUserAccessLevels().add(userAccessLevel);

        userRepository.save(user);
    }

    @Override
    public void registerUser(User user) throws ApplicationException {
        String passwordHash = passwordEncoder.encode(user.getPassword());

        user.setPassword(passwordHash);
        user.setActivationCode(UUID.randomUUID().toString().replace("-", ""));

        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevelRepository.findAccessLevelByName(CLIENT_ACCESS_LEVEL));

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
    public void activateUser(UUID activationCode) throws ApplicationException {

    }
}
