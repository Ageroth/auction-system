package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "access_level")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class AccessLevel extends BaseEntity {

    @Id
    @SequenceGenerator(name = "AccessLevelSeqGen", sequenceName = "access_level_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AccessLevelSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, updatable = false, length = 32)
    @NotNull
    private AccessLevelEnum name;
}