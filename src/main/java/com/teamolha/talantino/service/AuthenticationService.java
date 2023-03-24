package com.teamolha.talantino.service;

import com.teamolha.talantino.model.entity.Kind;
import com.teamolha.talantino.model.entity.Talent;
import com.teamolha.talantino.repository.KindRepository;
import com.teamolha.talantino.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public interface AuthenticationService {
    String generateJwtToken(Authentication authentication);

    public void register(String email, String password, String name, String surname, String kind);
}
