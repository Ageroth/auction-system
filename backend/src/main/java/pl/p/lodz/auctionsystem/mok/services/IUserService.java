package pl.p.lodz.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.p.lodz.auctionsystem.entities.User;
import pl.p.lodz.auctionsystem.exceptions.ApplicationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
@Service
public interface IUserService {
    /**
     * Metoda pozwalająca administratorowi na dodanie nowego użytkownika.
     *
     * @param user użytkownik do dodania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void createUser(User user) throws ApplicationException;

    /**
     * Metoda pozwalająca na samodzielną rejestrację użytkownika.
     *
     * @param user rejestrujący się użytkownik
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void registerUser(User user) throws ApplicationException;

    /**
     * Metoda zwracająca wszystkich użytkowników.
     *
     * @return lista użytkowników
     */
    List<User> getAllUsers();

    /**
     * Metoda zwracająca użytkownika o podanym id.
     *
     * @param userId ID użytkownika
     * @return użytkownik o podanym userId
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Optional<User> getUserById(Long userId) throws ApplicationException;

    /**
     * Metoda zwracająca użytkownika o podanym loginie.
     *
     * @param login login użytkownika
     * @return użytkownik o podanym loginie
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Optional<User> getUserByLogin(String login) throws ApplicationException;

//    /**
//     * Metoda, która pozwala użytkownikowi o podanym loginie, na zmianę hasła.
//     *
//     * @param login login użytkownika
//     * @throws ApplicationException wyjątek aplikacyjny, jeśli operacja nie zakończy się powodzeniem
//     */
//    void allowToChangePassword(String login) throws ApplicationException;
//
//    /**
//     * Metoda do zmiany hasła przez dowolnego użytkownika.
//     *
//     * @param resetPasswordCode kod zmiany hasła przypisany do użytkownika
//     * @param oldPassword       stare hasło
//     * @param newPassword       nowe hasło
//     * @throws ApplicationException wyjątek aplikacyjny, jeśli operacja nie zakończy się powodzeniem
//     */
//    void changePassword(UUID resetPasswordCode, String oldPassword, String newPassword) throws ApplicationException;
//
//    /**
//     * Metoda do zmiany hasła przez administratora.
//     *
//     * @param userId      id użytkownika, którego hasło ma zostać zmienione
//     * @param newPassword nowe hasło
//     * @throws ApplicationException wyjątek aplikacyjny, jeśli operacja nie zakończy się powodzeniem
//     */
//    void changePassword(Long userId, String newPassword) throws ApplicationException;

    /**
     * Metoda, która aktywuje konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @throws ApplicationException wyjątek aplikacyjny, jeśli operacja nie zakończy się powodzeniem
     */
    void activateUser(UUID activationCode) throws ApplicationException;
}
