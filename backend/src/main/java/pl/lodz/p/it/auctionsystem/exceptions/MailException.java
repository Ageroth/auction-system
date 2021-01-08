package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek dotyczący problemów z wysyłaniem wiadomości email.
 */
public class MailException extends ApplicationException {

    public MailException(String message) {
        super(message);
    }
}