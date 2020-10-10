package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserAccessLevelRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = ApplicationException.class)
public class UserAccessLevelServiceImpl implements UserAccessLevelService {
    
    private final UserAccessLevelRepository userAccessLevelRepository;
    private final UserRepository userRepository;
    private final AccessLevelRepository accessLevelRepository;
    
    @Autowired
    public UserAccessLevelServiceImpl(UserAccessLevelRepository userAccessLevelRepository,
                                      UserRepository userRepository, AccessLevelRepository accessLevelRepository) {
        this.userAccessLevelRepository = userAccessLevelRepository;
        this.userRepository = userRepository;
        this.accessLevelRepository = accessLevelRepository;
    }
    
    @Override
    public void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException {
        User user = userRepository.getOne(userId);
        AccessLevel accessLevel = accessLevelRepository.getOne(accessLevelId);

//        if (userAccessLevelRepository.existsByUserIdAndAccessLevelId(userId, accessLevel.getId())) {
//            throw userAlreadyHasGivenAccessLevelException(userId, accessLevel.getId());
//        }
        
        UserAccessLevel userAccessLevel = new UserAccessLevel(user, accessLevel);
        
        userAccessLevelRepository.save(userAccessLevel);
    }
    
    @Override
    public List<UserAccessLevel> getUserAccessLevelsByUserId(Long userId) {
        return userAccessLevelRepository.findByUser_Id(userId);
    }
    
    @Override
    public void deleteUserAccessLevel(Long userAccessLevelId) {
        userAccessLevelRepository.deleteById(userAccessLevelId);
    }
}
