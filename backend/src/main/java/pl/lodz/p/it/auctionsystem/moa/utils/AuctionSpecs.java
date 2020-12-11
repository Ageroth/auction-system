package pl.lodz.p.it.auctionsystem.moa.utils;

import org.springframework.data.jpa.domain.Specification;
import pl.lodz.p.it.auctionsystem.entities.Auction;

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

        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("item").get("name")), finalText)
        );
    }
}