package com.teamolha.talantino.account.service.impl;

import com.teamolha.talantino.account.mapper.AccountMapper;
import com.teamolha.talantino.account.repository.AccountRepository;
import com.teamolha.talantino.admin.mapper.AdminMapper;
import com.teamolha.talantino.admin.repository.AdminRepository;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.service.AuthService;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AdminRepository adminRepository;
    private TalentRepository talentRepository;
    private JwtEncoder jwtEncoder;
    private final SponsorRepository sponsorRepository;
    private TalentMapper talentMapper;
    private SponsorMapper sponsorMapper;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private AdminMapper adminMapper;

    @Override
    public Object myProfile(Authentication authentication) {
        var account = accountRepository.findByEmailIgnoreCase(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        AccountRole role = AccountRole.valueOf(account.getAuthorities().stream().findFirst().get());
        switch (role) {
            case TALENT -> {
                var talent = talentRepository.findByEmailIgnoreCase(account.getEmail()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
                return talentMapper.toTalentProfileResponse(
                        talent, talentRepository.findPrevTalent(talent.getId()),
                        talentRepository.findNextTalent(talent.getId()));
            }
            case SPONSOR -> {
                return sponsorMapper.toSponsorProfileResponse(
                        sponsorRepository.findByEmailIgnoreCase(account.getEmail()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)));
            }
            case ADMIN -> {
                return adminMapper.toAdminProfile(
                        adminRepository.findByEmailIgnoreCase(account.getEmail()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public LoginResponse login(Authentication authentication) {
        var user = accountRepository.findByEmailIgnoreCase(authentication.getName()).get();
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .claim("id", user.getId())
                .build();

        log.error("{}", authentication.isAuthenticated());

        return LoginResponse.builder()
                .id(user.getId())
                .token(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .name(user.getName())
                .surname(user.getSurname())
                .avatar(user.getAvatar())
                .build();

    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
