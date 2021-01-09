package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_access_level", uniqueConstraints = @UniqueConstraint(columnNames = {"access_level_id", "user_id"}))
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserAccessLevel extends BaseEntity {

    @Id
    @SequenceGenerator(name = "UserAccessLevelSeqGen", sequenceName = "user_access_level_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserAccessLevelSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @JoinColumn(name = "access_level_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    private AccessLevel accessLevel;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    private User user;

    public UserAccessLevel(User user, AccessLevel accessLevel) {
        super();
        this.user = user;
        this.accessLevel = accessLevel;
    }
}