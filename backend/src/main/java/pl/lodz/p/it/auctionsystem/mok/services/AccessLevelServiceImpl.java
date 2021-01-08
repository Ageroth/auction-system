package pl.lodz.p.it.auctionsystem.mok.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.AccessLevelDto;
import pl.lodz.p.it.auctionsystem.mok.repositories.AccessLevelRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationException.class)
@RequiredArgsConstructor
public class AccessLevelServiceImpl implements AccessLevelService {

    private final AccessLevelRepository accessLevelRepository;

    private final ModelMapper modelMapper;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<AccessLevelDto> getAllAccessLevels() {
        List<AccessLevel> accessLevels = accessLevelRepository.findAll();
        List<AccessLevelDto> accessLevelDtos = accessLevels.stream()
                .sorted(Comparator.comparing(AccessLevel::getId))
                .map(accessLevel -> modelMapper.map(accessLevel, AccessLevelDto.class))
                .collect(Collectors.toList());

        return accessLevelDtos;
    }
}