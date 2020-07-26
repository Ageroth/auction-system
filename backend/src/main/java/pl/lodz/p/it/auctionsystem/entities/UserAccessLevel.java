package pl.lodz.p.it.auctionsystem.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_access_level", uniqueConstraints = @UniqueConstraint(columnNames = {"access_level_id", "user_id"}))
@NoArgsConstructor
@Getter
public class UserAccessLevel extends BaseEntity {

    @Id
    @SequenceGenerator(name = "UserAccessLevelSeqGen", sequenceName = "user_access_level_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserAccessLevelSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @JoinColumn(name = "access_level_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private AccessLevel accessLevel;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public UserAccessLevel(User user, AccessLevel accessLevel) {
        super();
        this.user = user;
        this.accessLevel = accessLevel;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserAccessLevel)) return false;
        UserAccessLevel other = (UserAccessLevel) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
