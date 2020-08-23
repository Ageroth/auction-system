package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(name = "auction_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    private Auction auction;

    @Column(name = "bid_date", nullable = false, updatable = false)
    private LocalDateTime bidDate;

    @Column(name = "price", nullable = false, updatable = false)
    private BigDecimal price;
}