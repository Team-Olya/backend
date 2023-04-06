package com.teamolha.talantino.talent.service.impl;

import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import com.teamolha.talantino.talent.service.TalentAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TalentAuthServiceImpl implements TalentAuthService {
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
        var user = talentRepository.findByEmailIgnoreCase(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong authentication data!"));
        return new LoginResponse (
                user.getId(),
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
