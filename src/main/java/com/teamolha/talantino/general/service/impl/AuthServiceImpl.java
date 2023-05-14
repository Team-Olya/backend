package com.teamolha.talantino.general.service.impl;

import com.teamolha.talantino.account.mapper.AccountMapper;
import com.teamolha.talantino.account.repository.AccountRepository;
import com.teamolha.talantino.admin.repository.AdminRepository;
import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.general.service.AuthService;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.entity.Talent;
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

    @Override
    public Object myProfile(Authentication authentication) {
        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

//        if (authorities.contains(Roles.TALENT.name())) {
//            var talent = talentRepository.findByEmailIgnoreCase(authentication.getName())
//                    .orElseThrow(() ->
//                            new ResponseStatusException(HttpStatus.NOT_FOUND)
//                    );
//            return talentMapper.toTalentProfileResponse(
//                    talent, talentRepository.findPrevTalent(talent.getId()), talentRepository.findNextTalent(talent.getId())
//            );
//        }

        var account = accountRepository.findByEmailIgnoreCase(authentication.getName()).get();

//        if (authorities.contains(Roles.SPONSOR.name())) {
//            var sponsor = sponsorRepository.findByEmailIgnoreCase(authentication.getName())
//                    .orElseThrow(() ->
//                            new ResponseStatusException(HttpStatus.NOT_FOUND)
//                    );
//            return sponsorMapper.toSponsorProfileResponse(sponsor);
//        }
        Object user;
        switch (Roles.valueOf(authorities.get(0))) {
            case SPONSOR ->
                user = sponsorMapper.toSponsorProfileResponse(sponsorRepository.findByEmailIgnoreCase(account.getEmail()).get());
            case TALENT -> {
                var talent = talentRepository.findByEmailIgnoreCase(account.getEmail()).get();
                user = talentMapper.toTalentProfileResponse(talent,
                        talentRepository.findPrevTalent(talent.getTalentId()),
                        talentRepository.findNextTalent(talent.getTalentId())
                );
            }
            case ADMIN ->
                user = adminRepository.findByEmailIgnoreCase(account.getEmail()).get();
            default -> throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT,
                    "Account hasn't any role");
        }
        log.error(user.toString());
        return user;
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
