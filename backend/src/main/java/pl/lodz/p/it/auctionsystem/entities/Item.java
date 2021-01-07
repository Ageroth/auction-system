package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
    @NotNull
    @Setter
    private String name;

    @Column(name = "description", nullable = false, length = 4096)
    @NotNull
    @Setter
    private String description;

    @Column(name = "image", nullable = false, updatable = false)
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @NotNull
    private byte[] image;

    public Item(String name, String description, byte[] image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Item(Long version, UUID businessKey, Long id, String name, String description, byte[] image) {
        super(version, businessKey);
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}