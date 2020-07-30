package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;

import java.util.List;
import java.util.Optional;

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
    
    /**
     * Metoda aktywująca konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void activateUser(String activationCode) throws ApplicationException;
    
    /**
     * Metoda aktualizująca dane użytkownika o podanym id.
     *
     * @param user   dane użytkownika
     * @param userId id użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateUserDetails(User user, Long userId) throws ApplicationException;
    
    /**
     * Metoda aktualizująca dane użytkownika.
     *
     * @param user dane użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateUserDetails(User user) throws ApplicationException;
    
    /**
     * Metoda wysyłająca na podany email wiadomość z linkiem, pod którym można zresetować zapomniane hasło.
     *
     * @param email adres email użytkownika powiązany z kontem
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void sendResetPasswordMail(User user) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca zmianę zapomnianego hasła.
     *
     * @param user obiekt z danymi użytkownika potrzebnymi do zresetowania hasła
     * @throws ApplicationException
     */
    void resetPassword(User user) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca użytkownikowi zmianę własnego hasła.
     *
     * @param user        obiekt przechowujący dane wprowadzone w formularzu
     * @param oldPassword stare hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(User user, String oldPassword) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca administratorowi zmianę hasła innego użytkownika.
     *
     * @param user   obiekt przechowujący dane wprowadzone w formularzu
     * @param userId id użytkownika, którego hasło ma zostać zmienione
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(User user, Long userId) throws ApplicationException;
}
