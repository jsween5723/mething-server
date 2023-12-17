package com.esc.bluespring.domain.policyterm;


import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPolicytermRepository extends JpaRepository<UserPolicyterm, Long> {

    Set<UserPolicyterm> findByRequired(boolean isRequired);

    @Override
    @NotNull
    List<UserPolicyterm> findAll();

}
