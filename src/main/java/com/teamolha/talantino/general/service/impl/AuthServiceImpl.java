package com.teamolha.talantino.general.service.impl;

import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.general.service.AuthService;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.Mappers;
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
    private TalentRepository talentRepository;
    private JwtEncoder jwtEncoder;
    private final SponsorRepository sponsorRepository;
    private Mappers talentMapper;
    private SponsorMapper sponsorMapper;

    @Override
    public Object myProfile(Authentication authentication) {
        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (authorities.contains(Roles.TALENT.name())) {
            var talent = talentRepository.findByEmailIgnoreCase(authentication.getName())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND)
                    );
            return talentMapper.toTalentProfileResponse(
                    talent, talentRepository.findPrevTalent(talent.getId()), talentRepository.findNextTalent(talent.getId())
            );
        }

        if (authorities.contains(Roles.SPONSOR.name())) {
            var sponsor = sponsorRepository.findByEmailIgnoreCase(authentication.getName())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND)
                    );
            return sponsorMapper.toSponsorProfileResponse(sponsor);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public LoginResponse login(Authentication authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();

        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        log.error("{}", authentication.isAuthenticated());

        if (authorities.contains(Roles.TALENT.name())) {
            var talent = talentRepository.findByEmailIgnoreCase(authentication.getName())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Wrong authentication data!")
                    );
            return new LoginResponse(
                    talent.getId(),
                    jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                    talent.getName(),
                    talent.getSurname(),
                    talent.getAvatar()
            );
        }

        if (authorities.contains(Roles.SPONSOR.name())) {
            var sponsor = sponsorRepository.findByEmailIgnoreCase(authentication.getName())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Wrong authentication data")
                    );
            return new LoginResponse(
                    sponsor.getId(),
                    jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                    sponsor.getName(),
                    sponsor.getSurname(),
                    sponsor.getAvatar()
            );
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong authentication data");
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
