package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;

import java.util.List;

@Repository
@Transactional
public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevel, Long> {
    
    boolean existsByUser_IdAndAccessLevel_Id(Long userId, Long accessLevelId);
    
    List<UserAccessLevel> findByUser_Id(Long userId);
    
    void deleteById(Long id);
}