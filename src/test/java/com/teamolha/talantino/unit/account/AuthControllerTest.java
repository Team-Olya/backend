package com.teamolha.talantino.unit.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.account.controller.AuthController;
import com.teamolha.talantino.account.service.AuthService;
import com.teamolha.talantino.admin.model.response.AdminProfile;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.service.TalentService;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

//    @Autowired
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

        Mockito.when(authService.login(Mockito.any(Authentication.class))).thenReturn(expectedResponseBody);
        mockMvc.perform(post("/login").with(httpBasic(email, password))).andDo(print()).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void givenMyProfile_whenUserIsNotAuthenticated_thenReturns401() throws Exception {
        mockMvc.perform(get("/auth/me")).andExpect(status().isUnauthorized()).andExpect(unauthenticated()).andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenMyProfile_whenUserIsAuthenticated_thenReturns200() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get("/auth/me")).andExpect(status().isOk()).andReturn();
        AdminProfile expectedResponseBody = AdminProfile.builder()
                .id(1L)
                .role("ADMIN")
                .name("Admin")
                .surname("Adminchenko")
                .build();

        Mockito.when(authService.myProfile(Mockito.any(Authentication.class))).thenReturn(expectedResponseBody);
        mockMvc.perform(get("/auth/me")).andExpect(status().isOk()).andDo(print());
    }
}
