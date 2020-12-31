package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku, gdy użytkownik stawia ofertę na aukcji, której jest
 * właścicielem.
 */
public class AuctionOwnerException extends ApplicationException {

    public AuctionOwnerException(String message) {
        super(message);
    }
}