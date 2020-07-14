package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "\"user\"")
@SecondaryTable(name = "user_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@Getter
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "UserSeqGen", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "login", nullable = false, unique = true, updatable = false, length = 32)
    private String login;

    @Column(name = "password", nullable = false, length = 64)
    @Setter
    private String password;

    @Column(name = "email", nullable = false, unique = true, updatable = false, length = 64)
    private String email;

    @Column(name = "activated", nullable = false)
    @Setter
    private boolean activated;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "activation_code", nullable = false, unique = true, length = 128)
    @Setter
    private String activationCode;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "user")
    private Collection<UserAccessLevel> userAccessLevels = new ArrayList<>();

    @Column(name = "first_name", table = "user_details", nullable = false, length = 32)
    @Setter
    private String firstName;

    @Column(name = "last_name", table = "user_details", nullable = false, length = 32)
    @Setter
    private String lastName;

    @Column(name = "phone_number", table = "user_details", nullable = false, length = 10)
    @Setter
    private String phoneNumber;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
