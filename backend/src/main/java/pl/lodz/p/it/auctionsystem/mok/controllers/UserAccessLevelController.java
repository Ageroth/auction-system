package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.UserAccessLevelDto;
import pl.lodz.p.it.auctionsystem.mok.services.UserAccessLevelService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-access-levels")
public class UserAccessLevelController {
    
    private final UserAccessLevelService userAccessLevelService;
    
    private final ModelMapper modelMapper;
    
    private final MessageService messageService;
    
    @Autowired
    public UserAccessLevelController(UserAccessLevelService userAccessLevelService, ModelMapper modelMapper,
                                     MessageService messageService) {
        this.userAccessLevelService = userAccessLevelService;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAccessLevelsByUserId(@PathVariable Long userId) {
        List<UserAccessLevel> userAccessLevels = userAccessLevelService.getUserAccessLevelsByUserId(userId);
        List<UserAccessLevelDto> userAccessLevelDtos = userAccessLevels.stream()
                .map(userAccessLevel -> modelMapper.map(userAccessLevel, UserAccessLevelDto.class))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(userAccessLevelDtos, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> addUserAccessLevel(@Valid @RequestBody UserAccessLevelDto userAccessLevelDto) throws ApplicationException {
        userAccessLevelService.addUserAccessLevel(userAccessLevelDto.getUserId(),
                userAccessLevelDto.getAccessLevelId());
        
        String message = messageService.getMessage("userAccessLevel");
        
        return new ResponseEntity<>(new ApiResponseDto(true, message), HttpStatus.CREATED);
    }
    
    @DeleteMapping("{userAccessLevelId}")
    public ResponseEntity<?> deleteUserAccessLevel(@PathVariable("userAccessLevelId") Long userAccessLevelId) {
        userAccessLevelService.deleteUserAccessLevel(userAccessLevelId);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}