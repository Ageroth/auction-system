package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = ApplicationException.class)
public class AccessLevelServiceImpl implements AccessLevelService {
    
    private final AccessLevelRepository accessLevelRepository;
    
    @Autowired
    public AccessLevelServiceImpl(AccessLevelRepository accessLevelRepository) {
        this.accessLevelRepository = accessLevelRepository;
    }
    
    @Override
    public List<AccessLevel> getAllAccessLevels() {
        return accessLevelRepository.findAll();
    }
}
