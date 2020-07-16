package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "access_level")
@Getter
public class AccessLevel extends BaseEntity {

    @Id
    @SequenceGenerator(name = "AccessLevelSeqGen", sequenceName = "access_level_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AccessLevelSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, updatable = false, length = 32)
    private String name;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccessLevel)) return false;
        AccessLevel other = (AccessLevel) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
