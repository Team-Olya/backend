package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.admin.model.CreateAdmin;
import org.springframework.security.core.Authentication;

public interface AdminService {
    void createAdmin(CreateAdmin createAdmin);

    String login(Authentication authentication);

    void deleteTalent(String email);
}
