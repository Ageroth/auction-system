package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku podania niepoprawnego obecnego hasła.
 */
public class IncorrectPasswordException extends ApplicationException {

    public IncorrectPasswordException(String message) {
        super(message);
    }
}