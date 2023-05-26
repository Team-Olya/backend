package com.teamolha.talantino.unit.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.proof.controller.ProofController;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.skill.controller.SkillController;
import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.skill.model.response.SkillListDTO;
import com.teamolha.talantino.skill.service.SkillService;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.ProofAuthorDTO;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = SkillController.class)
@WithMockUser
@WebAppConfiguration
@ContextConfiguration
public class SkillControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    private String search;
    private Long proofId;
    private Long skillId;
    private int amount;
    private SkillListDTO skillListDTO;

    @BeforeEach
    public void setup() {
        List<SkillDTO> skillDTOList = new ArrayList<>();
        skillDTOList.add(SkillDTO.builder()
                .id(1L)
                .label("label1")
                .icon("icon1.svg")
                .build());
        skillDTOList.add(SkillDTO.builder()
                .id(2L)
                .label("label2")
                .icon("icon2.svg")
                .build());
        skillDTOList.add(SkillDTO.builder()
                        .id(3L)
                        .label("label3")
                        .icon("icon2.svg")
                .build());
        skillListDTO = SkillListDTO.builder()
                .skills(skillDTOList)
                .build();

        proofId = 3L;
        skillId = 1L;
        amount = 100;
        search = "lead";

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void givenSearch_thenStatusOkAndSkillListDTO() throws Exception {
        // Arrange
        String search = "java";
        SkillListDTO expectedResponse = skillListDTO;

        when(skillService.getSkillList(search)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/skills").param("search", search))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andDo(print());

        verify(skillService).getSkillList(search);
    }

    @Test
    public void givenProofId_thenStatusOkAndSkillListDTO() throws Exception {
        // Arrange
        long proofId = 123L;
        SkillListDTO expectedResponse = skillListDTO;

        when(skillService.getProofSkills(proofId)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/proofs/{proof-id}/skills", proofId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andDo(print());

        verify(skillService).getProofSkills(proofId);
    }

    @Test
    @WithMockUser
    public void givenProofIdAndSkillId_thenStatusOK() throws Exception {
        // Arrange
        long proofId = 123L;
        long skillId = 456L;
        int amount = 5;

        // Act & Assert
        mockMvc.perform(post("/proofs/{proof-id}/skills/{skill-id}/kudos", proofId, skillId)
                        .param("amount", String.valueOf(amount))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(skillService).setKudosToSkill(any(Authentication.class), eq(proofId), eq(skillId), eq(amount));
    }

}
