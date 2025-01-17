package pl.lodz.p.it.auctionsystem.mok.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;
import pl.lodz.p.it.auctionsystem.mok.services.UserAccessLevelService;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący operacje związane z użytkownikami.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserAccessLevelService userAccessLevelService;

    private final MessageService messageService;

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

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(new ApiResponseDto(true, message));
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

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Zwraca szczegóły naszego konta.
     *
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link OwnAccountDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/me")
    public ResponseEntity<?> getOwnDetails(Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        UserDto userDto = userService.getUserByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).eTag(String.valueOf(userDto.getVersion())).body(new OwnAccountDetailsDto(userDto));
    }

    /**
     * Zwraca użytkowników spełniających dane kryteria.
     *
     * @param userCriteria obiekt typu {@link UserCriteria}
     * @return Kod odpowiedzi HTTP 200 z listą stronnicowanych użytkowników, aktualnym numerem strony oraz całkowitą
     * ilością
     * użytkowników
     */
    @GetMapping
    public ResponseEntity<?> getUsers(UserCriteria userCriteria) {
        Page<BasicUserDto> userDtoPage = userService.getUsers(userCriteria);

        if (userDtoPage.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("users", userDtoPage.getContent());
            response.put("currentPage", userDtoPage.getNumber());
            response.put("totalItems", userDtoPage.getTotalElements());

            return ResponseEntity.status(HttpStatus.OK).body(response);
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
        UserDto userDto = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).eTag(String.valueOf(userDto.getVersion())).body(new UserAccountDetailsDto(userDto));
    }

    /**
     * Zwraca dane konta użytkownika o podanym kodzie resetu hasła.
     *
     * @param passwordResetCode kod resetu hasła przypisany do użytkownika
     * @return Kod odpowiedzi HTTP 200 z nagłówkiem ETag
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/password-reset/{passwordResetCode}")
    public ResponseEntity<?> getUser(@PathVariable(value = "passwordResetCode") String passwordResetCode) throws ApplicationException {
        UserDto userDto = userService.getUserByPasswordResetCode(passwordResetCode);

        return ResponseEntity.status(HttpStatus.OK).eTag(String.valueOf(userDto.getVersion())).build();
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie.
     *
     * @param username nazwa użytkownika
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/username-availability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(new UserIdentityAvailabilityDto(!userService.existsByUsername(username)));
    }

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanym adresie email.
     *
     * @param email adres email
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link UserIdentityAvailabilityDto}
     */
    @GetMapping("/email-availability")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam(value = "email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new UserIdentityAvailabilityDto(!userService.existsByEmail(email)));
    }

    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/activation/{activationCode}")
    public ResponseEntity<?> activateUser(@PathVariable(value = "activationCode") String activationCode) throws ApplicationException {
        userService.activateUser(activationCode);

        String message = messageService.getMessage("info.userActivated");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Wysyła użytkownikowi link umożliwiający mu przywrócenie konta w przypadku zapomnienia hasła.
     *
     * @param passwordResetEmailDto obiekt typu {@link PasswordResetEmailDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/password-reset")
    public ResponseEntity<?> sendPasswordResetEmail(@Valid @RequestBody PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException {
        userService.sendPasswordResetEmail(passwordResetEmailDto);

        String message = messageService.getMessage("info.passwordResetLinkSent");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło konta o podanym kodzie resetu hasła.
     *
     * @param passwordResetCode kod resetu hasła przypisany do użytkownika
     * @param passwordResetDto  obiekt typu {@link PasswordResetDto}
     * @param ifMatch           wartość pola wersji
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/password-reset/{passwordResetCode}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "passwordResetCode") String passwordResetCode,
                                           @Valid @RequestBody PasswordResetDto passwordResetDto,
                                           @RequestHeader(name = HttpHeaders.IF_MATCH) String ifMatch) throws ApplicationException {
        userService.resetPassword(passwordResetCode, passwordResetDto, ifMatch);

        String message = messageService.getMessage("info.passwordReset");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Aktualizuje nasze dane personalne.
     *
     * @param ownAccountDetailsUpdateDto obiekt typu {@link UserAccountDetailsUpdateDto}
     * @param ifMatch                    wartość pola wersji
     * @param authentication             obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/details")
    public ResponseEntity<?> updateOwnDetails(@Valid @RequestBody OwnAccountDetailsUpdateDto ownAccountDetailsUpdateDto,
                                              @RequestHeader(name = HttpHeaders.IF_MATCH) String ifMatch,
                                              Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        userService.updateDetailsByUsername(username, ownAccountDetailsUpdateDto, ifMatch);

        String message = messageService.getMessage("info.yourDetailsUpdated");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia nasze hasło.
     *
     * @param ownPasswordChangeDto obiekt typu {@link OwnPasswordChangeDto}
     * @param ifMatch              wartość pola wersji
     * @param authentication       obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/me/password")
    public ResponseEntity<?> changeOwnPassword(@Valid @RequestBody OwnPasswordChangeDto ownPasswordChangeDto,
                                               @RequestHeader(name = HttpHeaders.IF_MATCH) String ifMatch,
                                               Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        userService.changePasswordByUsername(username, ownPasswordChangeDto, ifMatch);

        String message = messageService.getMessage("info.yourPasswordChanged");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Aktualizuje dane personalne oraz role użytkownika o podanym id.
     *
     * @param userId                      id użytkownika
     * @param userAccountDetailsUpdateDto obiekt typu {@link UserAccountDetailsUpdateDto}
     * @param ifMatch                     wartość pola wersji obiektu
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{userId}/details")
    public ResponseEntity<?> updateUserDetails(@PathVariable(value = "userId") Long userId,
                                               @Valid @RequestBody UserAccountDetailsUpdateDto userAccountDetailsUpdateDto,
                                               @RequestHeader(name = HttpHeaders.IF_MATCH) String ifMatch) throws ApplicationException {
        userService.updateUserDetailsById(userId, userAccountDetailsUpdateDto, ifMatch);
        userAccessLevelService.modifyUserAccessLevels(userId, userAccountDetailsUpdateDto.getAccessLevelIds());

        String message = messageService.getMessage("info.userDetailsUpdated");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId                id użytkownika
     * @param userPasswordChangeDto obiekt typu {@link UserPasswordChangeDto}
     * @param ifMatch               wartość pola wersji obiektu
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable(value = "userId") Long userId,
                                                @RequestHeader(name = HttpHeaders.IF_MATCH) String ifMatch,
                                                @Valid @RequestBody UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException {
        userService.changePasswordById(userId, userPasswordChangeDto, ifMatch);

        String message = messageService.getMessage("info.userPasswordChanged");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }
}