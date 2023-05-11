package com.teamolha.talantino.talent.service;

import com.teamolha.talantino.talent.model.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TalentAuthService {
    LoginResponse login(Authentication authentication);

    LoginResponse login(String token);

    void register(String email, String password, String name, String surname, String kind, HttpServletRequest request);
}
