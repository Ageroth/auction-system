package pl.lodz.p.it.auctionsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@MappedSuperclass
@Getter
@EqualsAndHashCode
public abstract class BaseEntity {
    
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    
    @Column(name = "business_key", nullable = false, unique = true, updatable = false)
    private UUID businessKey;
    
    public BaseEntity() {
        this.businessKey = UUID.randomUUID();
    }
}
