package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.mok.utils.SortDirection;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    private final ModelMapper modelMapper;
    
    private final MessageService messageService;
    
    @Value("${pageSize}")
    private int pageSize;
    
    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, MessageService messageService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean status) {
    
        Direction direction = SortDirection.getSortDirection(order);
        List<User> users;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(direction, "createdAt"));
        Page<User> usersPage;
    
        if (query == null && status == null)
            usersPage = userService.getAllUsers(paging);
        else if (query != null && status == null)
            usersPage = userService.getFilteredUsers(query, paging);
        else if (query == null) {
            usersPage = userService.getFilteredUsers(status, paging);
        } else
            usersPage = userService.getFilteredUsers(query, status, paging);
    
    
        users = usersPage.getContent();
    
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());
    
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{userId}")
    public UserSummaryDto getUserProfile(@PathVariable(value = "userId") Long userId) throws ApplicationException {
        Optional<User> user = userService.getUserById(userId);
    
        return modelMapper.map(user.get(), UserSummaryDto.class);
    }
    
    @GetMapping("/me")
    public UserSummaryDto getCurrentUser(Authentication authentication) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        
        return modelMapper.map(currentUser, UserSummaryDto.class);
    }
    
    @GetMapping("/username-availability")
    public boolean checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return !userService.existsByUsername(username);
    }
    
    @GetMapping("/email-availability")
    public boolean checkEmailAvailability(@RequestParam(value = "email") String email) {
        return !userService.existsByEmail(email);
    }
    
    @PutMapping("/me/details")
    public ResponseEntity<?> updateOwnDetails(@Valid @RequestBody UpdateUserDetailsDto updateUserDetailsDto,
                                              Authentication authentication) throws ApplicationException {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = modelMapper.map(updateUserDetailsDto, User.class);
    
        userService.updateUserDetails(currentUser.getId(), user);
    
        String message = messageService.getMessage("userDetailsUpdated");
    
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    @PatchMapping("/me/password")
    public ResponseEntity<?> changeOwnPassword(@Valid @RequestBody ChangeOwnPasswordDto changeOwnPasswordDto,
                                               Authentication authentication) throws ApplicationException {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        
        userService.changePassword(currentUser.getId(), changeOwnPasswordDto.getNewPassword(),
                changeOwnPasswordDto.getOldPassword());
        
        String message = messageService.getMessage("userPasswordChanged");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    @PutMapping("/{userId}/details")
    public ResponseEntity<?> updateUserDetails(@PathVariable(value = "userId") Long userId,
                                               @Valid @RequestBody UpdateUserDetailsDto updateUserDetailsDto) throws ApplicationException {
        User user = modelMapper.map(updateUserDetailsDto, User.class);
        
        userService.updateUserDetails(userId, user);
        
        String message = messageService.getMessage("userDetailsUpdated");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable(value = "userId") Long userId,
                                                @Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto) throws ApplicationException {
        userService.changePassword(userId, changeUserPasswordDto.getNewPassword());
        
        String message = messageService.getMessage("userPasswordChanged");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}