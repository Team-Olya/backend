package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.admin.model.CreateAdmin;
import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.admin.repository.AdminRepository;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final JwtEncoder jwtEncoder;
    final PasswordEncoder passwordEncoder;
    AdminRepository adminRepository;
    TalentRepository talentRepository;
    LinkRepository linkRepository;
    ProofRepository proofRepository;

    @Override
    public void deleteTalent(String email) {
        var talent = talentRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent doesn't exist")
        );
        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
    }

    @Override
    public void createAdmin(CreateAdmin createAdmin) {
        if (adminRepository.count() == 0) {
            adminRepository.save(Admin.builder()
                    .login(createAdmin.login())
                    .password(passwordEncoder.encode(createAdmin.password()))
                    .authorities(List.of("ADMIN"))
                    .build()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create admin account, idiot hacker");
        }
    }

    public String login(Authentication authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        var user = adminRepository.findByLoginIgnoreCase(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong authentication data!"));
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
