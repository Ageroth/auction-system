package pl.lodz.p.it.auctionsystem.moa.utils;

import org.springframework.data.jpa.domain.Specification;
import pl.lodz.p.it.auctionsystem.entities.Auction;

import java.time.LocalDateTime;

public class AuctionSpecs {

    /**
     * Służy do znalezienia encji {@link Auction} zawierających daną frazę w nazwie przedmiotu.
     *
     * @param text poszukiwana fraza
     * @return obiekt typu {@link Specification<Auction>}
     */
    public static Specification<Auction> containsTextInName(String text) {
        if (!text.contains("%"))
            text = "%" + text + "%";

        String finalText = text.toLowerCase();

        return (root, query, builder) -> builder.like(builder.lower(root.get("item").get("name")), finalText);
    }

    /**
     * Służy do znalezienia encji {@link Auction} o danym statusie.
     *
     * @param auctionStatusEnum poszukiwany status
     * @return obiekt typu {@link Specification<Auction>}
     */
    public static Specification<Auction> hasStatus(AuctionStatusEnum auctionStatusEnum) {
        if (auctionStatusEnum.equals(AuctionStatusEnum.FINISHED))
            return (root, query, builder) -> builder.lessThan(root.get("endDate"), LocalDateTime.now());
        else
            return (root, query, builder) -> builder.greaterThan(root.get("endDate"), LocalDateTime.now());
    }

    /**
     * Służy do znalezienia encji {@link Auction}, których właścicielem jest użytkownik o danej nazwie użytkownika.
     *
     * @param username nazwa użytkownika właściciela
     * @return obiekt typu {@link Specification<Auction>}
     */
    public static Specification<Auction> isOwner(String username) {
        String finalText = username.toLowerCase();

        return (root, query, builder) -> builder.equal(builder.lower(root.get("user").get("username")), finalText);
    }
}