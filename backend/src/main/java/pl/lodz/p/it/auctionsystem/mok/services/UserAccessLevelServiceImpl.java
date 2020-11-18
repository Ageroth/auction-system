package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.exceptions.UserAccessLevelAlreadyExistsException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserAccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(rollbackFor = ApplicationException.class)
public class UserAccessLevelServiceImpl implements UserAccessLevelService {

    private final UserAccessLevelRepository userAccessLevelRepository;

    private final UserRepository userRepository;

    private final AccessLevelRepository accessLevelRepository;

    private final MessageService messageService;

    @Autowired
    public UserAccessLevelServiceImpl(UserAccessLevelRepository userAccessLevelRepository,
                                      UserRepository userRepository, AccessLevelRepository accessLevelRepository,
                                      MessageService messageService) {
        this.userAccessLevelRepository = userAccessLevelRepository;
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
        this.messageService = messageService;
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String accessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
        AccessLevel accessLevel =
                accessLevelRepository.findById(accessLevelId).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));

        if (userAccessLevelRepository.existsByUser_IdAndAccessLevel_Id(userId, accessLevelId)) {
            String userAccessLevelAlreadyExistsMessage = messageService.getMessage("exception" +
                    ".userAccessLevelAlreadyExists");

            throw new UserAccessLevelAlreadyExistsException(userAccessLevelAlreadyExistsMessage);
        }

        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);

        userAccessLevelRepository.save(userAccessLevel);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addUserAccessLevel(Long userId, String accessLevelName) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String accessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
        AccessLevel accessLevel =
                accessLevelRepository.findByName(AccessLevelEnum.valueOf(accessLevelName)).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);

        userAccessLevelRepository.save(userAccessLevel);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<UserAccessLevel> getUserAccessLevelsByUserId(Long userId) {
        return userAccessLevelRepository.findByUser_Id(userId);
    }

    @Override
    public void deleteUserAccessLevel(Long userAccessLevelId) {

    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void deleteUserAccessLevel(Long userId, String accessLevelName) throws ApplicationException {
        String userAccessLevelNotFoundMessage = messageService.getMessage("exception.userAccessLevelNotFound");
        UserAccessLevel userAccessLevel = userAccessLevelRepository.findByUser_IdAndAccessLevel_Name(userId, AccessLevelEnum.valueOf(accessLevelName))
                .orElseThrow(() -> new EntityNotFoundException(userAccessLevelNotFoundMessage));
        userAccessLevelRepository.deleteById(userAccessLevel.getId());
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void modifyUserAccessLevels(Long userId, List<String> userAccessLevelNames) throws ApplicationException {
        List<UserAccessLevel> oldUserAccessLevels = userAccessLevelRepository.findByUser_Id(userId);
        List<String> oldUserAccessLevelNames = oldUserAccessLevels.stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getName().toString()).collect(Collectors.toList());
        List<String> combined = Stream.concat(oldUserAccessLevelNames.stream(), userAccessLevelNames.stream())
                .collect(Collectors.toList());

        oldUserAccessLevelNames.retainAll(userAccessLevelNames);
        combined.removeAll(oldUserAccessLevelNames);

        System.out.println("HERE");
        for (String userAccessLevelName : combined) {
            if (userAccessLevelNames.contains(userAccessLevelName))
                addUserAccessLevel(userId, userAccessLevelName);
            else
                deleteUserAccessLevel(userId, userAccessLevelName);
        }
    }
}