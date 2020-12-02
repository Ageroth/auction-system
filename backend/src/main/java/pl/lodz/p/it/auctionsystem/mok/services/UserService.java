package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.*;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link User}
 */
@Service
public interface UserService {

    /**
     * Dodanie nowego użytkownika przez administratora.
     *
     * @param userAddDto użytkownik do dodania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long createUser(UserAddDto userAddDto) throws ApplicationException;

    /**
     * Samodzielna rejestracja przez użytkownika.
     *
     * @param signupDto rejestrujący się użytkownik
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long registerUser(SignupDto signupDto) throws ApplicationException;

    /**
     * Zwraca użytkowników.
     *
     * @param userCriteria obiekt typu {@link UserCriteria}
     * @return obiekt typu {@link Page<UserDto>} z użytkownikami
     */
    Page<UserDto> searchUsers(UserCriteria userCriteria);

    /**
     * Zwraca użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return użytkownik o podanym userId
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    UserDetailsDto getUserById(Long userId) throws ApplicationException;

    /**
     * Zwraca użytkownika o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return aktualnie zalogowany użytkownik
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    OwnDetailsDto getUserByUsername(String username) throws ApplicationException;

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return true/false w zależności od istnienia użytkownika w bazie
     */
    Boolean existsByUsername(String username);

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanym adresie email.
     *
     * @param email adres email
     * @return true/false w zależności od istnienia użytkownika w bazie
     */
    Boolean existsByEmail(String email);

    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void activateUser(String activationCode) throws ApplicationException;

    /**
     * Umożliwia aktualizację danych personalnych użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @param user   obiekt przechowujący nowe dane personalne
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateUserDetailsByUserId(Long userId, User user) throws ApplicationException;

    /**
     * Umożliwia aktualizację danych personalnych użytkownika o podanej nazwie użytkownika.
     *
     * @param ownDetailsUpdateDto obiekt typu {@link OwnDetailsUpdateDto}
     * @param username
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateDetailsByUsername(OwnDetailsUpdateDto ownDetailsUpdateDto, String username) throws ApplicationException;

    /**
     * Wysyła na podany email wiadomość z odnośnikiem, pod którym można zresetować zapomniane hasło.
     *
     * @param passwordResetEmailDto obiekt typu {@link PasswordResetEmailDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void sendPasswordResetEmail(PasswordResetEmailDto passwordResetEmailDto) throws ApplicationException;

    /**
     * Umożliwia zmianę zapomnianego hasła.
     *
     * @param passwordResetCode kod resetujący hasło
     * @param passwordResetDto  obiekt typu {@link PasswordResetDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void resetPassword(String passwordResetCode, PasswordResetDto passwordResetDto) throws ApplicationException;

    /**
     * Zmienia hasło aktualnie zalogowanego użytkownika.
     *
     * @param newPassword     nowe hasło
     * @param currentPassword obecne hasło
     * @param authentication  obiekt typu {@link Authentication}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(String newPassword, String currentPassword, Authentication authentication) throws ApplicationException;

    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId      id użytkownika
     * @param newPassword nowe hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(Long userId, String newPassword) throws ApplicationException;
}