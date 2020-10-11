package pl.lodz.p.it.auctionsystem.exceptions;

public class ResetPasswordCodeExpiredException extends ApplicationException {
    
    public ResetPasswordCodeExpiredException(String message) {
        super(message);
    }
}
