package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opinion")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Opinion extends BaseEntity {
    
    @Id
    @SequenceGenerator(name = "OpinionSeqGen", sequenceName = "opinion_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OpinionSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    
    @JoinColumn(name = "auction_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    private Auction auction;
    
    @Column(name = "rating", nullable = false)
    @Setter
    private int rating;
    
    @Column(name = "comment", length = 1024)
    @Setter
    private String comment;
    
    @Column(name = "date", nullable = false)
    @Setter
    private LocalDateTime date;
    
    @Column(name = "edited", nullable = false)
    @Setter
    private boolean edited;
}