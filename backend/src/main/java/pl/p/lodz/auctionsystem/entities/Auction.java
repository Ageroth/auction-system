package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction")
@SecondaryTable(name = "item", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@Getter
public class Auction extends BaseEntity {

    @Id
    @SequenceGenerator(name = "AuctionSeqGen", sequenceName = "auction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuctionSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @Column(name = "start_date", nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, updatable = false)
    private LocalDateTime endDate;

    @Column(name = "name", table = "item", nullable = false, updatable = false, length = 32)
    private String name;

    @Column(name = "starting_price", table = "item", nullable = false, updatable = false)
    private BigDecimal startingPrice;

    @Column(name = "description", table = "item", nullable = false, length = 4096)
    @Setter
    private String description;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Auction)) return false;
        Auction other = (Auction) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
