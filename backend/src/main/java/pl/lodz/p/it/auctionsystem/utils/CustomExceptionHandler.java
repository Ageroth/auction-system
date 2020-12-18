package pl.lodz.p.it.auctionsystem.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;

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
     * @return Kod odpowiedzi HTTP 404 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Obsługuje wyjątek {@link IncorrectPasswordException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link InvalidParameterException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link PasswordResetCodeExpiredException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(PasswordResetCodeExpiredException.class)
    public ResponseEntity<?> handlePasswordResetCodeExpiredException(PasswordResetCodeExpiredException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link UserAccessLevelAlreadyExistsException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(UserAccessLevelAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAccessLevelAlreadyExistsException(UserAccessLevelAlreadyExistsException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link ValueNotUniqueException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 422 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(ValueNotUniqueException.class)
    public ResponseEntity<?> handleValueNotUniqueException(ValueNotUniqueException ex) {
        return new ResponseEntity<>(new ApiResponseDto(false, ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Obsługuje wyjątek {@link MethodArgumentNotValidException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 422 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Obsługuje wyjątek {@link AuthenticationException}.
     *
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        String authenticationMessage = messageService.getMessage("exception.DisabledException");

        return new ResponseEntity<>(new ApiResponseDto(false, authenticationMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątek {@link InsufficientAuthenticationException}.
     *
     * @return Kod odpowiedzi HTTP 401 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException() {
        String insufficientAuthenticationMessage = messageService.getMessage("exception.insufficientAuthentication");

        return new ResponseEntity<>(new ApiResponseDto(false, insufficientAuthenticationMessage),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * Obsługuje wyjątek {@link BadCredentialsException}.
     *
     * @return Kod odpowiedzi HTTP 401 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        String badCredentialsMessage = messageService.getMessage("exception.BadCredentials");

        return new ResponseEntity<>(new ApiResponseDto(false, badCredentialsMessage), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Obsługuje wyjątek {@link DisabledException}.
     *
     * @return Kod odpowiedzi HTTP 403 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException() {
        String disabledMessage = messageService.getMessage("exception.DisabledException");

        return new ResponseEntity<>(new ApiResponseDto(false, disabledMessage), HttpStatus.FORBIDDEN);
    }
}