package pl.p.lodz.auctionsystem.entities;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Version
    @Column(name = "version", nullable = false)
    @Getter
    private long version;

    @Column(name = "business_key", nullable = false, unique = true, updatable = false)
    @Getter
    private UUID businessKey;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (businessKey != null ? businessKey.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [business_key=" + businessKey + " version=" + version + "]";
    }
}
