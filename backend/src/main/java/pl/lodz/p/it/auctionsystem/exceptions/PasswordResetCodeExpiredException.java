package pl.lodz.p.it.auctionsystem.exceptions;

public class PasswordResetCodeExpiredException extends ApplicationException {
    
    public PasswordResetCodeExpiredException(String message) {
        super(message);
    }
}