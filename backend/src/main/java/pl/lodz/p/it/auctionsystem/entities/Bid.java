package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Bid extends BaseEntity {

    @Id
    @SequenceGenerator(name = "BidSeqGen", sequenceName = "bid_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BidSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "date", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime date;

    @Column(name = "price", nullable = false, updatable = false)
    @NotNull
    private BigDecimal price;

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @JoinColumn(name = "auction_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    private Auction auction;

    public Bid(LocalDateTime date, BigDecimal price, User user, Auction auction) {
        this.date = date;
        this.price = price;
        this.user = user;
        this.auction = auction;
    }
}