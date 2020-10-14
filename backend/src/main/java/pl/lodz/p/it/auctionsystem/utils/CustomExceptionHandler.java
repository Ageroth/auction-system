package pl.lodz.p.it.auctionsystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.exceptions.IncorrectPasswordException;
import pl.lodz.p.it.auctionsystem.exceptions.ResetPasswordCodeExpiredException;

@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(value = IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = ResetPasswordCodeExpiredException.class)
    public ResponseEntity<?> handleResetPasswordCodeExpiredException(ResetPasswordCodeExpiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
