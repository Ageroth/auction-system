package pl.p.lodz.auctionsystem.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
@SecondaryTable(name = "user_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "UserSeqGen", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @Getter
    private Long id;

    @Column(name = "login", nullable = false, unique = true, updatable = false, length = 32)
    @Getter
    private String login;

    @Column(name = "password", nullable = false, length = 64)
    @Getter
    @Setter
    private String password;

    @Column(name = "email", nullable = false, unique = true, updatable = false, length = 64)
    @Getter
    private String email;

    @Column(name = "activated", nullable = false)
    @Getter
    @Setter
    private boolean activated;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime createdAt;

    @Column(name = "activation_code", nullable = false, unique = true, length = 128)
    @Getter
    @Setter
    private String activationCode;

    @Column(name = "first_name", table = "user_details", nullable = false, length = 32)
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name", table = "user_details", nullable = false, length = 32)
    @Getter
    @Setter
    private String lastName;

    @Column(name = "phone_number", table = "user_details", nullable = false, length = 10)
    @Getter
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
