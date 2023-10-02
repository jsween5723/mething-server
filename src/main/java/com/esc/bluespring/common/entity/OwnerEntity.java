package com.esc.bluespring.common.entity;

import com.esc.bluespring.common.exception.RequestException.NotOwnerException;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class OwnerEntity extends BaseEntity{
    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;

    public OwnerEntity(UUID id, Student owner) {
        super(id);
        this.owner = owner;
    }

    public void validOwner(Member member) {
        if (member.isAdmin()) {
            return;
        }
        if (!owner.getId().equals(member.getId())) {
            throw new NotOwnerException();
        }
    }
}
