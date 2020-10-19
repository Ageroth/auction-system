package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku ponownego wystąpienia wartości, która powinna być unikalna.
 */
public class ValueNotUniqueException extends ApplicationException {
    
    public ValueNotUniqueException(String message) { super(message); }
}