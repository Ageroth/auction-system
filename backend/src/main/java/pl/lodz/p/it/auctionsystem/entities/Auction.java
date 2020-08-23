package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    private User user;
    
    @JoinColumn(name = "item_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Item item;
    
    @Column(name = "start_date", nullable = false, updatable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date", nullable = false, updatable = false)
    private LocalDateTime endDate;
}