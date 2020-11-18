package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
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
     * @param signupDto obiekt typu {@link SignupDto}
     * @return HTTP status 201 z obiektem typu {@link ApiResponseDto}
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

        String message = messageService.getMessage("info.userRegistered");

        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/activation/{activationCode}")
    public ResponseEntity<?> activateUser(@PathVariable(value = "activationCode") String activationCode) throws ApplicationException {
        userService.activateUser(activationCode);

        String message = messageService.getMessage("info.userActivated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Wysyła użytkownikowi link umożliwiający mu przywrócenie konta w przypadku zapomnienia hasła.
     *
     * @param passwordResetEmailDto obiekt typu {@link PasswordResetEmailDto}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/password-reset")
    public ResponseEntity<?> sendPasswordResetEmail(@Valid @RequestBody PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        userService.sendPasswordResetEmail(passwordResetEmailDto.getEmail());

        String message = messageService.getMessage("info.passwordResetLinkSent");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło konta o podanym kodzie zmiany hasła.
     *
     * @param passwordResetCode kod zmiany hasła przypisany do użytkownika
     * @param passwordResetDto  obiekt typu {@link PasswordResetDto}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me/password-reset/{passwordResetCode}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "passwordResetCode") String passwordResetCode,
                                           @Valid @RequestBody PasswordResetDto passwordResetDto) throws ApplicationException {
        userService.resetPassword(passwordResetCode, passwordResetDto.getNewPassword());

        String message = messageService.getMessage("info.passwordReset");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Dodaje nowego użytkownika.
     *
     * @param signupDto obiekt typu {@link SignupDto}
     * @return HTTP status 201 z obiektem typu {@link ApiResponseDto}
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

        String message = messageService.getMessage("info.userAdded");

        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Pobiera listę użytkowników.
     *
     * @param page   numer strony do zwrócenia
     * @param order  porządek w jakim posortowani mają być użytkownicy
     * @param query  fraza wykorzystywana do wyszukania
     * @param status status aktywacji konta do filtrowania
     * @return HTTP status 200 z listą stronnicowanych użytkowników, aktualnym numerem strony, całkowitą ilością
     * użytkowników oraz liczbą wszystkich stron
     */
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean status) {
        List<User> users;
        List<UserDto> userDtos = new ArrayList<>();
        Pageable paging;
        Page<User> usersPage;

        if (sortField != null && order != null)
            paging = PageRequest.of(page, pageSize, Sort.by(SortDirection.getSortDirection(order), sortField));
        else
            paging = PageRequest.of(page, pageSize);

        if (query == null && status == null)
            usersPage = userService.getUsers(paging);
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

        for (User user : users) {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            userDto.setUserAccessLevelsName(user.getUserAccessLevels().stream()
                    .map(userAccessLevel -> userAccessLevel.getAccessLevel().getName().toString())
                    .collect(Collectors.toList()));

            userDtos.add(userDto);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("users", userDtos);
        response.put("currentPage", usersPage.getNumber());
        response.put("totalItems", usersPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Zwraca szczegóły naszego konta.
     *
     * @param authentication obiekt typu {@link Authentication}
     * @return HTTP status 200 z obiektem typu {@link UserSummaryDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyDetails(Authentication authentication) throws ApplicationException {
        User user = userService.getCurrentUser(authentication);
        UserSummaryDto userSummaryDto = modelMapper.map(user, UserSummaryDto.class);

        return new ResponseEntity<>(userSummaryDto, HttpStatus.OK);
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return HTTP status 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/username-availability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new ResponseEntity<>(new UserIdentityAvailabilityDto(!userService.existsByUsername(username)), HttpStatus.OK);
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanym adresie email.
     *
     * @param email adres email
     * @return HTTP status 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/email-availability")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new ResponseEntity<>(new UserIdentityAvailabilityDto(!userService.existsByEmail(email)), HttpStatus.OK);
    }

    /**
     * Aktualizuje nasze dane personalne.
     *
     * @param userDetailsUpdateDto obiekt typu {@link UserDetailsUpdateDto}
     * @param authentication       obiekt typu {@link Authentication}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/me/details")
    public ResponseEntity<?> updateOwnDetails(@Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto,
                                              Authentication authentication) throws ApplicationException {
        User user = modelMapper.map(userDetailsUpdateDto, User.class);

        userService.updateCurrentUserDetails(user, authentication);

        String message = messageService.getMessage("info.userDetailsUpdated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia nasze hasło.
     *
     * @param ownPasswordChangeDto obiekt typu {@link OwnPasswordChangeDto}
     * @param authentication       obiekt typu {@link Authentication}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password")
    public ResponseEntity<?> changeOwnPassword(@Valid @RequestBody OwnPasswordChangeDto ownPasswordChangeDto,
                                               Authentication authentication) throws ApplicationException {
        userService.changePassword(ownPasswordChangeDto.getNewPassword(),
                ownPasswordChangeDto.getOldPassword(), authentication);

        String message = messageService.getMessage("info.userPasswordChanged");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zwraca szczegóły konta użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return HTTP status 200 z obiektem typu {@link UserDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable(value = "userId") Long userId) throws ApplicationException {
        User user = userService.getUserById(userId);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        userDto.setUserAccessLevelsName(user.getUserAccessLevels().stream()
                .map(userAccessLevel -> userAccessLevel.getAccessLevel().getName().toString())
                .collect(Collectors.toList()));

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Aktualizuje dane personalne użytkownika o podanym id.
     *
     * @param userId               id użytkownika
     * @param userDetailsUpdateDto obiekt typu {@link UserDetailsUpdateDto}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/{userId}/details")
    public ResponseEntity<?> updateUserDetails(@PathVariable(value = "userId") Long userId,
                                               @Valid @RequestBody UserDetailsUpdateDto userDetailsUpdateDto) throws ApplicationException {
        User user = modelMapper.map(userDetailsUpdateDto, User.class);
        
        userService.updateUserDetailsByUserId(userId, user);

        String message = messageService.getMessage("info.userDetailsUpdated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId                id użytkownika
     * @param userPasswordChangeDto obiekt typu {@link UserPasswordChangeDto}
     * @return HTTP status 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable(value = "userId") Long userId,
                                                @Valid @RequestBody UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException {
        userService.changePassword(userId, userPasswordChangeDto.getNewPassword());

        String message = messageService.getMessage("info.userPasswordChanged");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}