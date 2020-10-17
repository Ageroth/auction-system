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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.mok.utils.SortDirection;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kontroler obsługujący operacje związane z użytkownikami.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    private final ModelMapper modelMapper;
    
    private final MessageService messageService;
    
    @Value("${page.size}")
    private int pageSize;
    
    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, MessageService messageService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }
    
    /**
     * Odpowiada za samodzielną rejestrację użytkownika.
     *
     * @param signupDto obiekt DTO z danymi rejestrującego się użytkownika
     * @return HTTP Status 201 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupDto signupDto) throws ApplicationException {
        User user = new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail(), signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getPhoneNumber());
        
        User result = userService.registerUser(user);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();
        
        String message = messageService.getMessage("userRegistered");
        
        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }
    
    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/activation/{activationCode}")
    public ResponseEntity<?> activateUser(@PathVariable(value = "activationCode") String activationCode) throws ApplicationException {
        userService.activateUser(activationCode);
        
        String message = messageService.getMessage("userActivated");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Wysyła użytkownikowi link umożliwiający mu przywrócenie konta w przypadku zapomnienia hasła.
     *
     * @param passwordResetEmailDto obiekt DTO z danymi
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/password-reset")
    public ResponseEntity<?> sendPasswordResetMail(@Valid @RequestBody PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        userService.sendPasswordResetMail(passwordResetEmailDto.getEmail());
        
        String message = messageService.getMessage("passwordResetLinkSent");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Zmienia hasło do konta o podanym kodzie zmiany hasła.
     *
     * @param passwordResetCode kod zmiany hasła przypisany do użytkownika
     * @param passwordResetDto  obiekt DTO z danymi reprezentującymi stare i nowe hasło
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/password-reset/{passwordResetCode}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "passwordResetCode") String passwordResetCode,
                                           @Valid @RequestBody PasswordResetDto passwordResetDto) throws ApplicationException {
        userService.resetPassword(passwordResetCode, passwordResetDto.getNewPassword());
        
        String message = messageService.getMessage("passwordReset");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Umożliwia dodanie nowego użytkownika przez administratora.
     *
     * @param signupDto obiekt DTO z danymi użytkownika do dodania.
     * @return HTTP Status 201 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupDto signupDto) throws ApplicationException {
        User user = new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail(), signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getPhoneNumber());
        
        User result = userService.createUser(user);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();
        
        String message = messageService.getMessage("userAdded");
        
        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }
    
    /**
     * Pobiera listę użytkowników przez administratora.
     *
     * @param page   numer strony do zwrócenia
     * @param order  porządek w jakim posortowani mają być użytkownicy
     * @param query  fraza wykorzystywana do wyszukania
     * @param status status aktywacji konta do filtrowania
     * @return HTTP Status 200 z listą stronnicowanych użytkowników, aktualnym numerem strony, całkowitą ilością
     * użytkowników oraz liczbą wszystkich stron
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean status) {
        
        Direction direction = SortDirection.getSortDirection(order);
        List<User> users;
        List<UserDto> userDtos;
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
        
        userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        
        
        Map<String, Object> response = new HashMap<>();
        
        response.put("users", userDtos);
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Zwraca szczegóły konta użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return HTTP Status 200 ze szczegółami dotyczącymi konta danego użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable(value = "userId") Long userId) throws ApplicationException {
        User user = userService.getUserById(userId);
        UserSummaryDto userSummaryDto = modelMapper.map(user, UserSummaryDto.class);
        
        return new ResponseEntity<>(userSummaryDto, HttpStatus.OK);
    }
    
    /**
     * Zwraca szczegóły naszego konta.
     *
     * @return HTTP Status 200 ze szczegółami dotyczącymi naszego konta
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        UserSummaryDto userSummaryDto = modelMapper.map(currentUser, UserSummaryDto.class);
        
        return new ResponseEntity<>(userSummaryDto, HttpStatus.OK);
    }
    
    @GetMapping("/username-availability")
    public boolean checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return !userService.existsByUsername(username);
    }
    
    @GetMapping("/email-availability")
    public boolean checkEmailAvailability(@RequestParam(value = "email") String email) {
        return !userService.existsByEmail(email);
    }
    
    /**
     * Aktualizuje nasze dane personalne.
     *
     * @param userDetailsUpdateDto obiekt DTO z naszymi, zaktualizowanymi danymi personalnymi
     * @param authentication       przechowuje informacje o aktualnie zalogowanym użytkowniku
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/me/details")
    public ResponseEntity<?> updateOwnDetails(@Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto,
                                              Authentication authentication) throws ApplicationException {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = modelMapper.map(userDetailsUpdateDto, User.class);
        
        userService.updateUserDetails(currentUser.getId(), user);
        
        String message = messageService.getMessage("userDetailsUpdated");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Zmienia nasze hasło.
     *
     * @param ownPasswordChangeDto obiekt DTO z nowym oraz starym hasłem, potrzebnym do weryfikacji
     * @param authentication       przechowuje informacje o aktualnie zalogowanym użytkowniku
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password")
    public ResponseEntity<?> changeOwnPassword(@Valid @RequestBody OwnPasswordChangeDto ownPasswordChangeDto,
                                               Authentication authentication) throws ApplicationException {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        
        userService.changePassword(currentUser.getId(), ownPasswordChangeDto.getNewPassword(),
                ownPasswordChangeDto.getOldPassword());
        
        String message = messageService.getMessage("userPasswordChanged");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Aktualizuje dane personalne użytkownika o podanym id.
     *
     * @param userId               id użytkownika
     * @param userDetailsUpdateDto obiekt DTO z zaktualizowanymi danymi personalnymi użytkownika
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/{userId}/details")
    public ResponseEntity<?> updateUserDetails(@PathVariable(value = "userId") Long userId,
                                               @Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto) throws ApplicationException {
        User user = modelMapper.map(userDetailsUpdateDto, User.class);
        
        userService.updateUserDetails(userId, user);
        
        String message = messageService.getMessage("userDetailsUpdated");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
    
    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId                id użytkownika
     * @param userPasswordChangeDto obiekt DTO z nowym hasłem
     * @return HTTP status 200 z informacją o powodzeniu operacji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable(value = "userId") Long userId,
                                                @Valid @RequestBody UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException {
        userService.changePassword(userId, userPasswordChangeDto.getNewPassword());
        
        String message = messageService.getMessage("userPasswordChanged");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}