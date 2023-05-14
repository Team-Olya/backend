package com.teamolha.talantino.general.service.impl;

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
    private TalentRepository talentRepository;
    private JwtEncoder jwtEncoder;
    private final SponsorRepository sponsorRepository;
    private TalentMapper talentMapper;
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
        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var user = authorities.contains(Roles.TALENT.name()) ? talentRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Wrong authentication data!")
                ) : sponsorRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Wrong authentication data")
                );

        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .claim("id", user instanceof Talent ? ((Talent) user).getId() : ((Sponsor) user).getId())
                .build();

        log.error("{}", authentication.isAuthenticated());

        return user instanceof Talent ? new LoginResponse(
                ((Talent) user).getId(),
                jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                ((Talent) user).getName(),
                ((Talent) user).getSurname(),
                ((Talent) user).getAvatar()
        ) : new LoginResponse(
                ((Sponsor) user).getId(),
                jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                ((Sponsor) user).getName(),
                ((Sponsor) user).getSurname(),
                ((Sponsor) user).getAvatar());

    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
