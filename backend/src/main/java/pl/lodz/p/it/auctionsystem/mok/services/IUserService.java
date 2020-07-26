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
}
