package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "image")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class Image extends BaseEntity {

    @Id
    @SequenceGenerator(name = "ImageSeqGen", sequenceName = "image_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ImageSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Item item;

    @Column(name = "lob", nullable = false, updatable = false)
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] lob;
}