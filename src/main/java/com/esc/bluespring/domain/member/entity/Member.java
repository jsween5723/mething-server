package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Table(name = "members", uniqueConstraints = {
    @UniqueConstraint(name = "email_constraint", columnNames = "email")}, indexes = {
    @Index(name = "email_index", columnList = "email", unique = true)})
@Hidden
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Member extends BaseEntity implements UserDetails {

  static public final String ADMIN = "ADMIN";
  static public final String STUDENT = "STUDENT";
  static public final String ANONYMOUS = "ANONYMOUS";
  static public final String NOT_CERTIFICATED_STUDENT = "NOT_CERTIFICATED_STUDENT";
  @Column(nullable = false, unique = true)
  protected String email;
  @Column(nullable = false, length = 800)
  protected String password;

  public Member(UUID id) {
    super(id);
  }

  public Member(UUID id, String email, String password) {
    super(id);
    this.email = email;
    this.password = password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRole();
  }

  @Transient
  abstract public List<Role> getRole();

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

  abstract public void valid();

  public void changePassword(String password) {
    this.password = password;
  }

  public void patch(Member source) {
    if (source.email != null) {
      email = source.email;
    }
    if (source.password != null) {
      password = source.getPassword();
    }
  }

  public enum Role implements GrantedAuthority {
    ADMIN, STUDENT, NOT_CERTIFICATED_STUDENT, CERTIFICATED_STUDENT;
    @Override
    public String getAuthority() {
      return "ROLE_" + name();
    }
  }
}
