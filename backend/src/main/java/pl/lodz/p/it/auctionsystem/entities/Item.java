package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseEntity {

    @Id
    @SequenceGenerator(name = "ItemSeqGen", sequenceName = "item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ItemSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false, length = 32)
    @Setter
    @NotNull
    private String name;

    @Column(name = "description", nullable = false, length = 4096)
    @Setter
    @NotNull
    private String description;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "item")
    @NotNull
    Set<Image> images = new HashSet<>();
}