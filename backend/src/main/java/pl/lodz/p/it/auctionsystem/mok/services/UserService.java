package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;

@SuppressWarnings("ALL")
@Service
public interface UserService {
    /**
     * Metoda pozwalająca administratorowi na dodanie nowego użytkownika.
     *
     * @param user użytkownik do dodania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User createUser(User user) throws ApplicationException;
    
    /**
     * Metoda pozwalająca na samodzielną rejestrację użytkownika.
     *
     * @param user rejestrujący się użytkownik
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User registerUser(User user) throws ApplicationException;
    
    /**
     * Metoda zwracająca wszystkich użytkowników.
     *
     * @return lista użytkowników
     */
    Page<User> getAllUsers(Pageable pageable);
    
    Page<User> getFilteredUsers(String query, boolean status, Pageable pageable);
    
    Page<User> getFilteredUsers(String query, Pageable pageable);
    
    Page<User> getFilteredUsers(boolean status, Pageable pageable);
    
    /**
     * Metoda zwracająca użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return użytkownik o podanym userId
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User getUserById(Long userId) throws ApplicationException;
    
    /**
     * Metoda zwracająca użytkownika o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return użytkownik o podanej nazwie użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User getUserByUsername(String username) throws ApplicationException;
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    /**
     * Metoda aktywująca konto użytkownika o przypisanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny przypisany do konta użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void activateUser(String activationCode) throws ApplicationException;
    
    
    void updateUserDetails(Long userId, User user) throws ApplicationException;
    
    /**
     * Metoda wysyłająca na podany email wiadomość z linkiem, pod którym można zresetować zapomniane hasło.
     *
     * @param email adres email użytkownika powiązany z kontem
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void sendPasswordResetMail(String email) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca zmianę zapomnianego hasła.
     *
     * @param user obiekt z danymi użytkownika potrzebnymi do zresetowania hasła
     * @throws ApplicationException
     */
    void resetPassword(String resetPasswordCode, String newPassword) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca użytkownikowi zmianę własnego hasła.
     *
     * @param user        obiekt przechowujący dane wprowadzone w formularzu
     * @param oldPassword stare hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(Long userId, String newPassword, String oldPassword) throws ApplicationException;
    
    /**
     * Metoda umożliwiająca administratorowi zmianę hasła innego użytkownika.
     *
     * @param user   obiekt przechowujący dane wprowadzone w formularzu
     * @param userId id użytkownika, którego hasło ma zostać zmienione
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(Long userId, String newPassword) throws ApplicationException;
}