package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "auction")
@SecondaryTable(name = "item", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Auction extends BaseEntity {

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY)
    private final Collection<Bid> bids = new ArrayList<>();

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
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @Column(name = "name", table = "item", nullable = false, length = 32)
    @NotNull
    @Setter
    private String itemName;

    @Column(name = "description", table = "item", nullable = false, length = 4096)
    @NotNull
    @Setter
    private String itemDescription;

    @Column(name = "image", table = "item", nullable = false, updatable = false)
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @NotNull
    private byte[] itemImage;

    public Auction(BigDecimal startingPrice, LocalDateTime startDate,
                   LocalDateTime endDate, User user, String itemName,
                   String itemDescription, byte[] itemImage) {
        this.startingPrice = startingPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
    }

    public Auction(Long version, UUID businessKey, Long id, BigDecimal startingPrice,
                   LocalDateTime startDate, LocalDateTime endDate, User user,
                   String itemName, String itemDescription, byte[] itemImage) {
        super(version, businessKey);
        this.id = id;
        this.startingPrice = startingPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
    }
}