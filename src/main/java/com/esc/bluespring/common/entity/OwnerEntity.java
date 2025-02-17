package com.esc.bluespring.common.entity;

import com.esc.bluespring.common.exception.RequestException.NotOwnerException;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class OwnerEntity<M extends Member> extends BaseEntity{
    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private M owner;

    public OwnerEntity(UUID id, M owner) {
        super(id);
        this.owner = owner;
    }

    public void validOwner(Member member) {
        if (!isOwner(member)) {
            throw new NotOwnerException();
        }
    }

    public boolean isOwner(Member member) {
        if (member.isAdmin()) {
            return true;
        }
        return owner.getId().equals(member.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OwnerEntity<?> that = (OwnerEntity<?>) o;
        return Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }
}
