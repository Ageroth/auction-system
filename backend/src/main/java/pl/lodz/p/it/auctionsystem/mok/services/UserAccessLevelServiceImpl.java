package pl.lodz.p.it.auctionsystem.mok.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.exceptions.UserAccessLevelAlreadyExistsException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserAccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepositoryMok;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationException.class)
@RequiredArgsConstructor
public class UserAccessLevelServiceImpl implements UserAccessLevelService {

    private final UserAccessLevelRepository userAccessLevelRepository;

    private final UserRepositoryMok userRepositoryMok;

    private final AccessLevelRepository accessLevelRepository;

    private final MessageService messageService;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user = userRepositoryMok.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String accessLevelNotFoundMessage = messageService.getMessage("exception.accessLevelNotFound");
        AccessLevel accessLevel =
                accessLevelRepository.findById(accessLevelId).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));

        if (userAccessLevelRepository.existsByUserIdAndAccessLevelId(userId, accessLevelId)) {
            String userAccessLevelAlreadyExistsMessage = messageService.getMessage("exception" +
                    ".userAccessLevelAlreadyExists");

            throw new UserAccessLevelAlreadyExistsException(userAccessLevelAlreadyExistsMessage);
        }

        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);

        userAccessLevelRepository.save(userAccessLevel);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void deleteUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException {
        String userAccessLevelNotFoundMessage = messageService.getMessage("exception.userAccessLevelNotFound");
        UserAccessLevel userAccessLevel = userAccessLevelRepository.findByUserIdAndAccessLevelId(userId,
                accessLevelId)
                .orElseThrow(() -> new EntityNotFoundException(userAccessLevelNotFoundMessage));

        userAccessLevelRepository.deleteById(userAccessLevel.getId());
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void modifyUserAccessLevels(Long userId, List<Long> accessLevelIds) throws ApplicationException {
        List<UserAccessLevel> oldUserAccessLevels = userAccessLevelRepository.findByUserId(userId);
        List<Long> oldUserAccessLevelIds = oldUserAccessLevels.stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getId()).collect(Collectors.toList());
        List<Long> combined = Stream.concat(oldUserAccessLevelIds.stream(), accessLevelIds.stream())
                .collect(Collectors.toList());

        oldUserAccessLevelIds.retainAll(accessLevelIds);
        combined.removeAll(oldUserAccessLevelIds);

        for (Long accessLevelId : combined) {
            if (accessLevelIds.contains(accessLevelId))
                addUserAccessLevel(userId, accessLevelId);
            else
                deleteUserAccessLevel(userId, accessLevelId);
        }
    }
}