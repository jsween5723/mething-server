package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Table(name = "members")
@Hidden
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Member extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String email;
    @Column(nullable = false, length = 800)
    private String password;

    public Member(Long id) {
        super(id);
    }

    public Member(Long id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getClass().equals(Admin.class) ? Role.ADMIN : Role.STUDENT);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public boolean isAdmin() {
        return getClass().equals(Admin.class);
    }

    public void valid() {
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public enum Role implements GrantedAuthority {
        ADMIN, STUDENT;

        @Override
        public String getAuthority() {
            return name();
        }
    }
}
