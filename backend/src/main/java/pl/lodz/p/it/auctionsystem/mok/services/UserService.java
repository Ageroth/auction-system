package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.data.domain.Page;
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
     * Rejestruje użytkownika.
     *
     * @param signupDto obiekt typu {@link SignupDto}
     * @return id nowo utworzonego obiektu
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long registerUser(SignupDto signupDto) throws ApplicationException;

    /**
     * Tworzy użytkownika.
     *
     * @param userAddDto obiekt typu {@link UserAddDto}
     * @return id nowo utworzonego obiektu
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long addUser(UserAddDto userAddDto) throws ApplicationException;

    /**
     * Zwraca użytkownika o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return obiekt typu {@link OwnAccountDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    OwnAccountDetailsDto getUserByUsername(String username) throws ApplicationException;

    /**
     * Zwraca użytkowników spełniających zadane kryteria.
     *
     * @param userCriteria obiekt typu {@link UserCriteria}
     * @return obiekt typu {@link Page<UserDto>}
     */
    Page<UserDto> searchUsers(UserCriteria userCriteria);

    /**
     * Zwraca użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return obiekt typu {@link UserAccountDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    UserAccountDetailsDto getUserById(Long userId) throws ApplicationException;

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return true/false w zależności od istnienia użytkownika w bazie
     */
    boolean existsByUsername(String username);

    /**
     * Sprawdza czy w bazie istnieje użytkownik o podanym adresie email.
     *
     * @param email adres email
     * @return true/false w zależności od istnienia użytkownika w bazie
     */
    boolean existsByEmail(String email);

    /**
     * Aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void activateUser(String activationCode) throws ApplicationException;

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
     * Umożliwia aktualizację danych personalnych użytkownika o podanej nazwie.
     *
     * @param username                   nazwa użytkownika
     * @param ownAccountDetailsUpdateDto obiekt typu {@link OwnAccountDetailsUpdateDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateDetailsByUsername(String username, OwnAccountDetailsUpdateDto ownAccountDetailsUpdateDto) throws ApplicationException;

    /**
     * Aktualizuje dane personalne użytkownika o podanym id.
     *
     * @param userId                      id użytkownika
     * @param userAccountDetailsUpdateDto obiekt typu {@link UserAccountDetailsUpdateDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateUserDetailsById(Long userId, UserAccountDetailsUpdateDto userAccountDetailsUpdateDto) throws ApplicationException;

    /**
     * Zmienia hasło użytkownika o podanej nazwie.
     *
     * @param username             nazwa użytkownika
     * @param ownPasswordChangeDto obiekt typu {@link OwnPasswordChangeDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePasswordByUsername(String username, OwnPasswordChangeDto ownPasswordChangeDto) throws ApplicationException;

    /**
     * Zmienia hasło użytkownika o podanym id.
     *
     * @param userId                id użytkownika
     * @param userPasswordChangeDto obiekt typu {@link UserPasswordChangeDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePasswordById(Long userId, UserPasswordChangeDto userPasswordChangeDto) throws ApplicationException;
}