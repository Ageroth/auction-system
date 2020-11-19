package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku, gdy nowe hasło jest takei same jak stare.
 */
public class PasswordIdenticalException extends ApplicationException {

    public PasswordIdenticalException(String message) {
        super(message);
    }
}