package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Getter
public class Item extends BaseEntity {

    @Id
    @SequenceGenerator(name = "ItemSeqGen", sequenceName = "item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", table = "item", nullable = false, updatable = false, length = 32)
    private String name;

    @Column(name = "starting_price", table = "item", nullable = false, updatable = false)
    private BigDecimal startingPrice;

    @Column(name = "description", table = "item", nullable = false, length = 4096)
    @Setter
    private String description;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Item)) return false;
        Item other = (Item) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
