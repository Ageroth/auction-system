package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa bazowa dla wszystkich wyjątków aplikacyjnych.
 */
public abstract class ApplicationException extends Exception {

    public ApplicationException(String message) {
        super(message);
    }
}