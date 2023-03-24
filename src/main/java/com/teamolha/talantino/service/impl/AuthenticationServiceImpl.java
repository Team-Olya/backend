package com.teamolha.talantino.service.impl;

import com.teamolha.talantino.model.entity.Kind;
import com.teamolha.talantino.model.entity.Talent;
import com.teamolha.talantino.model.response.LoginResponse;
import com.teamolha.talantino.repository.KindRepository;
import com.teamolha.talantino.repository.TalentRepository;
import com.teamolha.talantino.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtEncoder jwtEncoder;
    private TalentRepository talentRepository;
    private KindRepository kindRepository;
    final PasswordEncoder passwordEncoder;

    public LoginResponse login(Authentication authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        var user = talentRepository.findByEmailIgnoreCase(authentication.getName()).get();
        return new LoginResponse (
                jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                user.getName(),
                user.getSurname(),
                user.getAvatar()
        );
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    public void register(String email, String password, String name, String surname, String kind) {
        if(talentRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    email + " is already occupied!"
            );
        }
        if(!kindRepository.existsByKindIgnoreCase(kind)) {
            kindRepository.save(
                    Kind.builder()
                            .kind(kind)
                            .build()
            );
        }
        talentRepository.save(
                Talent.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .name(name)
                        .surname(surname)
                        .kind(kindRepository.findByKindIgnoreCase(kind))
                        .build()
        );
    }
}
