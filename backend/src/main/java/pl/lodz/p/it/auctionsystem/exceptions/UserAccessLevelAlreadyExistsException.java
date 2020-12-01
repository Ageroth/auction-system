package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku próby nadania użytkownikowi poziomu dostępu, który już
 * posiada.
 */
public class UserAccessLevelAlreadyExistsException extends ApplicationException {

    public UserAccessLevelAlreadyExistsException(String message) {
        super(message);
    }
}