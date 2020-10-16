package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku wygaśnięcia ważności kodu resetującego hasło.
 */
public class PasswordResetCodeExpiredException extends ApplicationException {
    
    public PasswordResetCodeExpiredException(String message) { super(message); }
}