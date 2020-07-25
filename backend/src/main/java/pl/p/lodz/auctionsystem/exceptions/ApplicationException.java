package pl.p.lodz.auctionsystem.exceptions;

import lombok.Getter;

/**
 * Klasa bazowa dla wszystkich wyjątków aplikacyjnych.
 */
@Getter
public abstract class ApplicationException extends Exception {

    private final String errorCode;

    public ApplicationException(String errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}