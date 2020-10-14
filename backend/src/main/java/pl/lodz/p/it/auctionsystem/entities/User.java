package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "\"user\"")
@SecondaryTable(name = "user_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    @Id
    @SequenceGenerator(name = "UserSeqGen", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSeqGen")
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, updatable = false, length = 32)
    private String username;
    
    @Column(name = "password", nullable = false, length = 64)
    @Setter
    private String password;
    
    @Column(name = "email", nullable = false, unique = true, updatable = false, length = 64)
    private String email;
    
    @Column(name = "activated", nullable = false)
    @Setter
    private boolean activated;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "activation_code", unique = true, length = 128)
    @Setter
    private String activationCode;
    
    @Column(name = "password_reset_code", unique = true, length = 64)
    @Setter
    private String passwordResetCode;
    
    @Column(name = "password_reset_code_add_date")
    @Setter
    private LocalDateTime passwordResetCodeAddDate;
    
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
    
    public User(String username, String password, String email, String firstName, String lastName, String phoneNumber) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.activated = false;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }
}