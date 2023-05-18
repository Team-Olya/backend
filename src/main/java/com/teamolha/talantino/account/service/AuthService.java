package com.teamolha.talantino.account.service;

import com.teamolha.talantino.talent.model.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponse login(Authentication authentication);

    Object myProfile(Authentication email);

    LoginResponse login(String token);
}
