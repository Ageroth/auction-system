package pl.lodz.p.it.auctionsystem.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link IncorrectPasswordException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link InvalidParameterException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link PasswordResetCodeExpiredException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(PasswordResetCodeExpiredException.class)
    public ResponseEntity<?> handlePasswordResetCodeExpiredException(PasswordResetCodeExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link UserAccessLevelAlreadyExistsException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(UserAccessLevelAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAccessLevelAlreadyExistsException(UserAccessLevelAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link ValueNotUniqueException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 422 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(ValueNotUniqueException.class)
    public ResponseEntity<?> handleValueNotUniqueException(ValueNotUniqueException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link MethodArgumentNotValidException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 422 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder = new StringBuilder();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            stringBuilder.append(fieldName).append(':').append(errorMessage).append("\n");
        });

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponseDto(false,
                stringBuilder.toString()));
    }

    /**
     * Obsługuje wyjątek {@link AuthenticationException}.
     *
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        String authenticationMessage = messageService.getMessage("exception.authenticationException");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, authenticationMessage));
    }

    /**
     * Obsługuje wyjątek {@link InsufficientAuthenticationException}.
     *
     * @return Kod odpowiedzi HTTP 401 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<?> handleInsufficientAuthenticationException() {
        String insufficientAuthenticationMessage = messageService.getMessage("exception.insufficientAuthentication");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDto(false,
                insufficientAuthenticationMessage));
    }

    /**
     * Obsługuje wyjątek {@link BadCredentialsException}.
     *
     * @return Kod odpowiedzi HTTP 401 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        String badCredentialsMessage = messageService.getMessage("exception.badCredentials");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDto(false, badCredentialsMessage));
    }

    /**
     * Obsługuje wyjątek {@link DisabledException}.
     *
     * @return Kod odpowiedzi HTTP 403 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException() {
        String disabledMessage = messageService.getMessage("exception.disabledException");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponseDto(false, disabledMessage));
    }

    /**
     * Obsługuje wyjątek {@link AccessForbiddenException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 403 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(AccessForbiddenException.class)
    public ResponseEntity<?> handleAccessForbiddenException(AccessForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link InvalidDateException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<?> handleInvalidDateException(InvalidDateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link AuctionOwnerException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(AuctionOwnerException.class)
    public ResponseEntity<?> handleAuctionOwnerException(AuctionOwnerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link InvalidBidPriceException}.
     *
     * @param ex obiekt wyjątku
     * @return Kod odpowiedzi HTTP 400 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(InvalidBidPriceException.class)
    public ResponseEntity<?> handleInvalidBidPriceException(InvalidBidPriceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(false, ex.getMessage()));
    }

    /**
     * Obsługuje wyjątek {@link OptimisticLockingFailureException}.
     *
     * @return Kod odpowiedzi HTTP 412 z obiektem {@link ApiResponseDto}
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> handleOptimisticLockingFailureException() {
        String optimisticLockingFailureExceptionMessage = messageService.getMessage("exception" +
                ".optimisticLockingFailureException");

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new ApiResponseDto(false,
                optimisticLockingFailureExceptionMessage));
    }
}