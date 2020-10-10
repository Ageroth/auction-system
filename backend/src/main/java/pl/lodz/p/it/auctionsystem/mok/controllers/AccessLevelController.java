package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.mok.dtos.AccessLevelDto;
import pl.lodz.p.it.auctionsystem.mok.services.AccessLevelService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/access-levels")
public class AccessLevelController {
    
    private final AccessLevelService accessLevelService;
    
    private final ModelMapper modelMapper;
    
    @Autowired
    public AccessLevelController(AccessLevelService accessLevelService, ModelMapper modelMapper) {
        this.accessLevelService = accessLevelService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllAccessLevels() {
        List<AccessLevel> accessLevels = accessLevelService.getAllAccessLevels();
        List<AccessLevelDto> accessLevelDtos = accessLevels.stream()
                .map(accessLevel -> modelMapper.map(accessLevel, AccessLevelDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(accessLevelDtos, HttpStatus.OK);
    }
}