package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku wystąpienia niepoprawnej daty.
 */
public class InvalidDateException extends ApplicationException {

    public InvalidDateException(String message) { super(message); }
}