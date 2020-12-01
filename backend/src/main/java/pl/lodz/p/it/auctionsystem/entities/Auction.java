package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "auction")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Auction extends BaseEntity {

    @Id
    @SequenceGenerator(name = "AuctionSeqGen", sequenceName = "auction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuctionSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "starting_price", nullable = false)
    @NotNull
    @Setter
    private BigDecimal startingPrice;

    @Column(name = "start_date", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime endDate;

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    @NotNull
    private User user;

    @JoinColumn(name = "item_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private Item item;

    @OneToMany(mappedBy = "auction")
    private final Collection<Bid> bids = new ArrayList<>();

    public Auction(BigDecimal startingPrice, LocalDateTime startDate,
                   LocalDateTime endDate, User user, Item item) {
        this.startingPrice = startingPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.item = item;
    }
}