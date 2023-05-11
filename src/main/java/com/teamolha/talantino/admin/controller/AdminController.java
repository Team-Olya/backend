package com.teamolha.talantino.admin.controller;

import com.teamolha.talantino.admin.model.CreateAdmin;
import com.teamolha.talantino.admin.model.DeleteTalent;
import com.teamolha.talantino.admin.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@AllArgsConstructor
public class AdminController {

    AdminService adminService;

    @PostMapping("/admin/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin (@RequestBody CreateAdmin createAdmin) {
        adminService.createAdmin(createAdmin);
    }

    @PostMapping("/admin/login")
    public String login (Authentication authentication) {
        return adminService.login(authentication);
    }

    @DeleteMapping("/admin/delete/talents")
    public void deleteTalent(@RequestBody DeleteTalent deleteTalent, Authentication auth) {
        var authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (authorities.contains("ADMIN")) {
            adminService.deleteTalent(deleteTalent.email());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not admin");
        }
    }
}
