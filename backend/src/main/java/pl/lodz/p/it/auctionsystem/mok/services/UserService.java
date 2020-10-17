package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link User}
 */
@Service
public interface UserService {
    
    /**
     * Dodanie nowego użytkownika przez administratora.
     *
     * @param user użytkownik do dodania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User createUser(User user) throws ApplicationException;
    
    /**
     * Samodzielna rejestracja przez użytkownika.
     *
     * @param user rejestrujący się użytkownik
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User registerUser(User user) throws ApplicationException;
    
    /**
     * Zwraca użytkowników.
     *
     * @param pageable obiekt typu {@link Pageable}
     * @return obiekt typu {@link Page} z użytkownikami
     */
    Page<User> getUsers(Pageable pageable);
    
    /**
     * Zwraca przefiltrowanych użytkowników.
     *
     * @param query    fraza wykorzystywana do wyszukania
     * @param status   status aktywacji konta do filtrowania
     * @param pageable obiekt typu {@link Pageable}
     * @return obiekt typu {@link Page} z użytkownikami
     */
    Page<User> getFilteredUsers(String query, boolean status, Pageable pageable);
    
    /**
     * Zwraca przefiltrowanych użytkowników.
     *
     * @param query    fraza wykorzystywana do wyszukania
     * @param pageable obiekt typu {@link Pageable}
     * @return obiekt typu {@link Page} z użytkownikami
     */
    Page<User> getFilteredUsers(String query, Pageable pageable);
    
    /**
     * Zwraca przefiltrowanych użytkowników.
     *
     * @param status   status aktywacji konta do filtrowania
     * @param pageable obiekt typu {@link Pageable}
     * @return obiekt typu {@link Page} z użytkownikami
     */
    Page<User> getFilteredUsers(boolean status, Pageable pageable);
    
    /**
     * Zwraca użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return użytkownik o podanym userId
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User getUserById(Long userId) throws ApplicationException;
    
    /**
     * Zwraca użytkownika o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return użytkownik o podanej nazwie użytkownika
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    User getUserByUsername(String username) throws ApplicationException;
    
    /**
     * Sprawdza czy istnieje użytkownik o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return true jeśli istnieje, w przeciwnym wypadku false
     */
    boolean existsByUsername(String username);
    
    /**
     * Sprawdza czy istnieje użytkownik o podanej nazwie użytkownika.
     *
     * @param email adres email użytkownika
     * @return true jeśli istnieje, w przeciwnym wypadku false
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
     * Umożliwia aktualizację danych personalnych użytkownika.
     *
     * @param userId id użytkownika
     * @param user   obiekt przechowujący nowe dane personalne
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateUserDetails(Long userId, User user) throws ApplicationException;
    
    /**
     * Wysyła na podany email wiadomość z odnośnikiem, pod którym można zresetować zapomniane hasło.
     *
     * @param email adres email użytkownika powiązany z kontem
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void sendPasswordResetMail(String email) throws ApplicationException;
    
    /**
     * Umożliwia zmianę zapomnianego hasła.
     *
     * @param passwordResetCode kod resetujący hasło
     * @param newPassword       nowe hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void resetPassword(String passwordResetCode, String newPassword) throws ApplicationException;
    
    /**
     * Umożliwia użytkownikowi zmianę własnego hasła.
     *
     * @param userId      id użytkownika
     * @param newPassword nowe hasło
     * @param oldPassword stare hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(Long userId, String newPassword, String oldPassword) throws ApplicationException;
    
    /**
     * Umożliwia administratorowi zmianę hasła innego użytkownika.
     *
     * @param userId      id użytkownika
     * @param newPassword nowe hasło
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void changePassword(Long userId, String newPassword) throws ApplicationException;
}