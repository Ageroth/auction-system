package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import java.util.List;

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
    public void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("userNotFound");
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String accessLevelNotFoundMessage = messageService.getMessage("accessLevelNotFound");
        AccessLevel accessLevel =
                accessLevelRepository.findById(accessLevelId).orElseThrow(() -> new EntityNotFoundException(accessLevelNotFoundMessage));
    
        if (userAccessLevelRepository.existsByUser_IdAndAccessLevel_Id(userId, accessLevelId)) {
            String userAccessLevelAlreadyExistsMessage = messageService.getMessage("userAccessLevelAlreadyExists");
        
            throw new UserAccessLevelAlreadyExistsException(userAccessLevelAlreadyExistsMessage);
        }
    
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);
    
        userAccessLevelRepository.save(userAccessLevel);
    }
    
    @Override
    public List<UserAccessLevel> getUserAccessLevelsByUserId(Long userId) {
        return userAccessLevelRepository.findByUser_Id(userId);
    }
    
    @Override
    public void deleteUserAccessLevel(Long userAccessLevelId) {
        if (userAccessLevelRepository.existsById(userAccessLevelId))
            userAccessLevelRepository.deleteById(userAccessLevelId);
    }
}