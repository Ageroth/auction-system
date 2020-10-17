package pl.lodz.p.it.auctionsystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.auctionsystem.exceptions.*;

/**
 * Klasa obsługująca wyjątki aplikacyjne.
 */
@ControllerAdvice
public class CustomExceptionHandler {
    
    /**
     * Obsługuje wyjątek {@link EntityNotFoundException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 404
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    /**
     * Obsługuje wyjątek {@link IncorrectPasswordException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(value = IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Obsługuje wyjątek {@link InvalidParameterException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(value = InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Obsługuje wyjątek {@link PasswordResetCodeExpiredException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(value = PasswordResetCodeExpiredException.class)
    public ResponseEntity<?> handlePasswordResetCodeExpiredException(PasswordResetCodeExpiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Obsługuje wyjątek {@link UserAccessLevelAlreadyExistsException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(value = UserAccessLevelAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAccessLevelAlreadyExistsException(UserAccessLevelAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}