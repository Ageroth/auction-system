package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseEntity {
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "item")
    @NotNull
    Set<Image> images = new HashSet<>();
    
    @Id
    @SequenceGenerator(name = "ItemSeqGen", sequenceName = "item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    
    @Column(name = "name", table = "item", nullable = false, updatable = false, length = 32)
    @NotNull
    private String name;
    
    @Column(name = "starting_price", table = "item", nullable = false, updatable = false)
    @NotNull
    private BigDecimal startingPrice;
    
    @Column(name = "description", table = "item", nullable = false, length = 4096)
    @Setter
    @NotNull
    private String description;
}