package com.teamolha.talantino.unit.proof;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.proof.controller.ProofController;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.ProofAuthorDTO;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProofController.class)
public class ProofControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @MockBean
    private ProofService proofService;

    private Kind kind;

    private Talent talent;

    private ProofDTO proofDTO;

    @BeforeEach
    public void setup() {
        proofDTO = ProofDTO.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .title("Something title")
                .description("Something description")
                .author(ProofAuthorDTO.builder()
                        .id(1L).avatar("avatar")
                        .name("Name").surname("Surname").build())
                .status("PUBLISHED")
                .totalKudos(null)
                .totalKudosFromSponsor(null)
                .isKudosed(false)
                .skills(Collections.singletonList(
                        ProofSkillDTO.builder().id(1L).label("Skill").icon("icon")
                                .totalKudos(null).totalKudosFromSponsor(null).isKudosed(false).build())
                )
                .build();

        kind = Kind.builder()
                .id(1L)
                .kind("Java Developer")
                .talents(null)
                .build();

        talent = Talent.builder()
                .id(1L)
                .accountStatus(AccountStatus.ACTIVE)
                .verificationToken(null)
                .verificationExpireDate(null)
                .kind(kind)
                .password("123456")
                .name("Name")
                .surname("Surname")
                .avatar(null)
                .deletionDate(null)
                .deletionToken(null)
                .email("test@mail.com")
                .authorities(Collections.singleton("TALENT"))
                .build();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    public void givenTalentsProofs_whenValidUrlAndMethodAndUserIsAuthenticated_thenReturns200() throws Exception {
        mockMvc.perform(get("/talents/1/proofs")).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void givenTalentsProofs_whenValidUrlAndMethodAndUserIsNotAuthenticated_thenReturns401() throws Exception {
        mockMvc.perform(get("/talents/1/proofs")).andExpect(status().isUnauthorized()).andExpect(unauthenticated());
    }

    @Test
    @WithMockUser
    public void givenTalentsProofs_whenValidInput_thenReturns200() throws Exception {
        Long talentId = 1L;
        String sort = "date";
        String type = "desc";
        String status = "PUBLISHED";
        Integer amount = 9;
        Integer page = 0;

        mockMvc.perform(get("/talents/{talent-id}/proofs", talentId)
                .param("sort", sort)
                .param("type", type)
                .param("status", status)
                .param("amount", String.valueOf(amount))
                .param("page", String.valueOf(page))
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenTalentsProofs_whenInvalidInput_thenReturns400() throws Exception {
        Long talentId = 1L;
        String sort = "bate";
        String type = "cesc";
        String status = "PUBLISHED";
        Integer amount = 9;
        Integer page = 0;

        mockMvc.perform(get("/talents/{talent-id}/proofs", talentId)
                .param("sort", sort)
                .param("type", type)
                .param("status", status)
                .param("amount", String.valueOf(amount))
                .param("page", String.valueOf(page))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TALENT")
    public void givenTalentsProofs_whenValidInputAndUserIsAuthentication_thenReturns200AndTalentProofList() throws Exception {
        Long talentId = 1L;
        String sort = "date";
        String type = "asc";
        String status = "PUBLISHED";
        Integer amount = 9;
        Integer page = 0;

        TalentProofList expectedResponseBody = TalentProofList.builder()
                .totalAmount(1L)
                .proofs(Collections.singletonList(proofDTO))
                .build();

        when(proofService.talentProofs(any(Authentication.class), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedResponseBody);
        MvcResult mvcResult = mockMvc.perform(get("/talents/{talent-id}/proofs", talentId)
                .queryParam("sort", sort)
                .queryParam("type", type)
                .queryParam("status", status)
                .queryParam("amount", String.valueOf(amount))
                .queryParam("page", String.valueOf(page))
        ).andExpect(status().isOk()).andDo(print()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));
    }

    @Test
    @WithAnonymousUser
    public void givenTalentsProofs_whenValidInputAndUserIsNotAuthenticated_thenReturns401() throws Exception {
        Long talentId = 1L;
        String sort = "date";
        String type = "asc";
        String status = "PUBLISHED";
        Integer amount = 9;
        Integer page = 0;

        TalentProofList expectedResponseBody = TalentProofList.builder()
                .totalAmount(1L)
                .proofs(Collections.singletonList(proofDTO))
                .build();

        when(proofService.talentProofs(any(Authentication.class), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedResponseBody);
        mockMvc.perform(get("/talents/{talent-id}/proofs", talentId)
                .queryParam("sort", sort)
                .queryParam("type", type)
                .queryParam("status", status)
                .queryParam("amount", String.valueOf(amount))
                .queryParam("page", String.valueOf(page))
        ).andExpect(status().isUnauthorized()).andExpect(unauthenticated()).andDo(print());
    }
}
