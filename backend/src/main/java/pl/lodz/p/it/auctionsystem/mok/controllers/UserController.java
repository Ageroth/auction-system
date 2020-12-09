package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.services.UserAccessLevelService;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący operacje związane z użytkownikami.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserAccessLevelService userAccessLevelService;

    private final MessageService messageService;

    @Autowired
    public UserController(UserService userService, UserAccessLevelService userAccessLevelService,
                          MessageService messageService) {
        this.userService = userService;
        this.userAccessLevelService = userAccessLevelService;
        this.messageService = messageService;
    }

    /**
     * Odpowiada za samodzielną rejestrację użytkownika.
     *
     * @param signupDto obiekt typu {@link SignupDto}
     * @return Kod odpowiedzi HTTP 201 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/me")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupDto signupDto) throws ApplicationException {
        Long userId = userService.registerUser(signupDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(userId).toUri();
        String message = messageService.getMessage("info.userRegistered");

        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Dodaje nowego użytkownika.
     *
     * @param userAddDto obiekt typu {@link UserAddDto}
     * @return Kod odpowiedzi HTTP 201 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserAddDto userAddDto) throws ApplicationException {
        Long userId = userService.addUser(userAddDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(userId).toUri();

        String message = messageService.getMessage("info.userAdded");

        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Zwraca szczegóły naszego konta.
     *
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link OwnAccountDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyDetails(Authentication authentication) throws ApplicationException {
        OwnAccountDetailsDto ownAccountDetailsDto =
                userService.getUserByUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername());

        return new ResponseEntity<>(ownAccountDetailsDto, HttpStatus.OK);
    }

    /**
     * Pobiera użytkowników spełniających dane kryteria.
     *
     * @param userCriteria obiekt typu {@link UserCriteria}
     * @return Kod odpowiedzi HTTP 200 z listą stronnicowanych użytkowników, aktualnym numerem strony, całkowitą ilością
     * użytkowników oraz liczbą wszystkich stron
     */
    @GetMapping
    public ResponseEntity<?> getUsers(UserCriteria userCriteria) {
        Page<UserDto> userDtoPage = userService.getUsers(userCriteria);

        if (userDtoPage.getContent().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("users", userDtoPage.getContent());
            response.put("currentPage", userDtoPage.getNumber());
            response.put("totalItems", userDtoPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    /**
     * Zwraca szczegóły konta użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link UserAccountDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable(value = "userId") Long userId) throws ApplicationException {
        UserAccountDetailsDto userAccountDetailsDto = userService.getUserById(userId);

        return new ResponseEntity<>(userAccountDetailsDto, HttpStatus.OK);
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie.
     *
     * @param username nazwa użytkownika
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/username-availability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new ResponseEntity<>(new UserIdentityAvailabilityDto(!userService.existsByUsername(username)),
                HttpStatus.OK);
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanym adresie email.
     *
     * @param email adres email
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/email-availability")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new ResponseEntity<>(new UserIdentityAvailabilityDto(!userService.existsByEmail(email)), HttpStatus.OK);
    }

    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/activation/{activationCode}")
    public ResponseEntity<?> activateUser(@PathVariable(value = "activationCode") String activationCode) throws ApplicationException {
        userService.activateUser(activationCode);

        String message = messageService.getMessage("info.userActivated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Wysyła użytkownikowi link umożliwiający mu przywrócenie konta w przypadku zapomnienia hasła.
     *
     * @param passwordResetEmailDto obiekt typu {@link PasswordResetEmailDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password-reset")
    public ResponseEntity<?> sendPasswordResetEmail(@Valid @RequestBody PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        userService.sendPasswordResetEmail(passwordResetEmailDto);

        String message = messageService.getMessage("info.passwordResetLinkSent");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło konta o podanym kodzie resetu hasła.
     *
     * @param passwordResetCode kod resetu hasła przypisany do użytkownika
     * @param passwordResetDto  obiekt typu {@link PasswordResetDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password-reset/{passwordResetCode}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "passwordResetCode") String passwordResetCode,
                                           @Valid @RequestBody PasswordResetDto passwordResetDto) throws ApplicationException {
        userService.resetPassword(passwordResetCode, passwordResetDto);

        String message = messageService.getMessage("info.passwordReset");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Aktualizuje nasze dane personalne.
     *
     * @param ownAccountDetailsUpdateDto obiekt typu {@link UserAccountDetailsUpdateDto}
     * @param authentication             obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/me/details")
    public ResponseEntity<?> updateOwnDetails(@Valid @RequestBody OwnAccountDetailsUpdateDto ownAccountDetailsUpdateDto,
                                              Authentication authentication) throws ApplicationException {
        String currentUserUsername = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();

        userService.updateDetailsByUsername(currentUserUsername, ownAccountDetailsUpdateDto);

        String message = messageService.getMessage("info.yourDetailsUpdated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia nasze hasło.
     *
     * @param ownPasswordChangeDto obiekt typu {@link OwnPasswordChangeDto}
     * @param authentication       obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password")
    public ResponseEntity<?> changeOwnPassword(@Valid @RequestBody OwnPasswordChangeDto ownPasswordChangeDto,
                                               Authentication authentication) throws ApplicationException {
        String currentUserUsername = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();

        userService.changePasswordByUsername(currentUserUsername, ownPasswordChangeDto);

        String message = messageService.getMessage("info.yourPasswordChanged");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Aktualizuje dane personalne oraz role użytkownika o podanym id.
     *
     * @param userId                      id użytkownika
     * @param userAccountDetailsUpdateDto obiekt typu {@link UserAccountDetailsUpdateDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PutMapping("/{userId}/details")
    public ResponseEntity<?> updateUserDetails(@PathVariable(value = "userId") Long userId,
                                               @Valid @RequestBody UserAccountDetailsUpdateDto userAccountDetailsUpdateDto) throws ApplicationException {
        userService.updateUserDetailsById(userId, userAccountDetailsUpdateDto);
        userAccessLevelService.modifyUserAccessLevels(userId, userAccountDetailsUpdateDto.getAccessLevelIds());

        String message = messageService.getMessage("info.userDetailsUpdated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId                id użytkownika
     * @param userPasswordChangeDto obiekt typu {@link UserPasswordChangeDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable(value = "userId") Long userId,
                                                @Valid @RequestBody UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException {
        userService.changePasswordById(userId, userPasswordChangeDto);

        String message = messageService.getMessage("info.userPasswordChanged");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}