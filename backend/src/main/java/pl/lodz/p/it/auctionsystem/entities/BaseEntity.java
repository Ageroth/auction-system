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

    public BaseEntity() {}

    public BaseEntity(Long version) {
        this.version = version;
    }
}