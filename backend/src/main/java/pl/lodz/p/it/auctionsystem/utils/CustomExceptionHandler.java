package pl.lodz.p.it.auctionsystem.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa obsługująca wyjątki aplikacyjne.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final MessageService messageService;

    /**
     * Obsługuje wyjątek {@link EntityNotFoundException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 404
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Obsługuje wyjątek {@link IncorrectPasswordException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPassword(IncorrectPasswordException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link InvalidParameterException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameter(InvalidParameterException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link PasswordResetCodeExpiredException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(PasswordResetCodeExpiredException.class)
    public ResponseEntity<?> handlePasswordResetCodeExpired(PasswordResetCodeExpiredException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link UserAccessLevelAlreadyExistsException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 400
     */
    @ExceptionHandler(UserAccessLevelAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAccessLevelAlreadyExists(UserAccessLevelAlreadyExistsException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link ValueNotUniqueException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 422
     */
    @ExceptionHandler(ValueNotUniqueException.class)
    public ResponseEntity<?> handleValueNotUnique(ValueNotUniqueException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Obsługuje wyjątek {@link MethodArgumentNotValidException}.
     *
     * @param ex obiekt wyjątku
     * @return HTTP status 422
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    /**
     * Obsługuje wyjątek {@link InsufficientAuthenticationException}.
     *
     * @return HTTP status 401
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthentication() {
        String insufficientAuthenticationMessage = messageService.getMessage("exception.insufficientAuthentication");

        return new ResponseEntity<>(new ApiResponseDto(false, insufficientAuthenticationMessage), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Obsługuje wyjątek {@link BadCredentialsException}.
     *
     * @return HTTP status 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials() {
        String badCredentialsMessage = messageService.getMessage("exception.BadCredentials");

        return new ResponseEntity<>(new ApiResponseDto(false, badCredentialsMessage), HttpStatus.UNAUTHORIZED);
    }
}