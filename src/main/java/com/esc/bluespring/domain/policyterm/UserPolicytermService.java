package com.esc.bluespring.domain.policyterm;


import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPolicytermService {

    private final UserPolicytermRepository userPolicytermRepository;

    public List<UserPolicyterm> getBuyerPolicyterms() {
        return userPolicytermRepository.findAll();
    }

    public boolean isContainsRequiredPolicyterms(Set<UserPolicyterm> userPolicyterms) {
        Set<UserPolicyterm> required = userPolicytermRepository.findByRequired(true);
        return userPolicyterms.containsAll(required);
    }
}
