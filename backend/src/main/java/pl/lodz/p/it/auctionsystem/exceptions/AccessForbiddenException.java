package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek dotyczący braku dostępu do zasobu.
 */
public class AccessForbiddenException extends ApplicationException {
    public AccessForbiddenException(String message) {
        super(message);
    }
}