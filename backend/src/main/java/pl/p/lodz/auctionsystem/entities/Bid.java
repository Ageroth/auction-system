package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@NoArgsConstructor
@Getter
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Bid)) {
            return false;
        }
        Bid other = (Bid) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
