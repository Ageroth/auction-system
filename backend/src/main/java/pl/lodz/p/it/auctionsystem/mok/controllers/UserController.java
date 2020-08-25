package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.mok.utils.SortDirection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    private final MessageService messageService;
    
    @Value("${pageSize}")
    private int pageSize;
    
    @Autowired
    public UserController(UserService userService, MessageService messageService) {
        this.userService = userService;
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
    
    @GetMapping("/username-availability")
    public boolean checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return !userService.existsByUsername(username);
    }
    
    @GetMapping("/email-availability")
    public boolean checkEmailAvailability(@RequestParam(value = "email") String email) {
        return !userService.existsByEmail(email);
    }
}