package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku stawiania oferty niższej od aktualnej ceny przedmiotu
 * aukcji.
 */
public class InvalidBidPriceException extends ApplicationException {

    public InvalidBidPriceException(String message) {
        super(message);
    }
}