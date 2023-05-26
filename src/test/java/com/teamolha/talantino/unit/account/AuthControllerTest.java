package com.teamolha.talantino.unit.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.account.controller.AuthController;
import com.teamolha.talantino.account.service.AuthService;
import com.teamolha.talantino.admin.model.response.AdminProfile;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.model.request.CreateTalent;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.service.TalentService;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@WithMockUser
@WebAppConfiguration
@ContextConfiguration
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TalentService talentService;

    @MockBean
    private AuthService authService;

    @MockBean
    private SponsorService sponsorService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Disabled
    public void givenLogin_whenValidUrlAndMethodAndUserCredentials_thenReturns200AndLoginResponse() throws Exception {
        String email = "test@mail.com";
        String password = "123456";
        LoginResponse expectedResponseBody = LoginResponse.builder()
                .id(1L)
                .token("token")
                .name("Name")
                .surname("Surname")
                .avatar("avatar")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password,
                Collections.singleton(new SimpleGrantedAuthority("TALENT")));
        Mockito.when(authService.login(authentication)).thenReturn(expectedResponseBody);
        mockMvc.perform(post("/login").with(httpBasic(email, password))).andDo(print()).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void givenMyProfile_whenUserIsNotAuthenticated_thenReturns401() throws Exception {
        mockMvc.perform(get("/auth/me")).andExpect(status().isUnauthorized()).andExpect(unauthenticated()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void givenMyProfile_whenUserIsAuthenticated_thenReturns200() throws Exception {
        AdminProfile expectedResponseBody = AdminProfile.builder()
                .id(1L)
                .role("ADMIN")
                .name("Admin")
                .surname("Adminchenko")
                .build();

        Mockito.when(authService.myProfile(Mockito.any(Authentication.class))).thenReturn(expectedResponseBody);
        mockMvc.perform(get("/auth/me")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void givenTalentRegister_whenValidUrlAndContentType_thenReturns201() throws Exception {
        CreateTalent createTalent = new CreateTalent("test@mail.com", "123456qwe",
                "Name", "Surname", "Java Developer");

        mockMvc.perform(post("/talents/register")
                        .content(objectMapper.writeValueAsString(createTalent))
                        .with(csrf())
                        .characterEncoding("utf-8")
                        .contentType("application/json")).andDo(print())
                .andExpect(status().isCreated()).andDo(print());
    }
}
