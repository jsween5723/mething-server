package com.esc.bluespring.domain.member;

import com.esc.bluespring.domain.auth.exception.AuthException.LoginRequiredException;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.RefreshToken;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Transactional
    public UUID create(Member member) {
        RefreshToken refreshToken = new RefreshToken(member);
        return repository.save(refreshToken).getRefreshToken();
    }

    @Transactional
    public RefreshToken refresh(UUID refreshToken) {
        RefreshToken previous = repository.findById(refreshToken)
            .orElseThrow(LoginRequiredException::new);
        RefreshToken saved = repository.save(new RefreshToken(previous.getMember()));
        repository.delete(previous);
        return saved;
    }

    @Transactional
    public void delete(UUID token) {
        repository.deleteById(token);
    }
}
