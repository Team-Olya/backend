package com.teamolha.talantino.unit.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.admin.controller.AdminController;
import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.service.AdminService;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.response.KindDTO;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
@WithMockUser
@WebAppConfiguration
@ContextConfiguration
public class AdminControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void givenCreateAdmin_whenValidRequestBody_thenReturnsCreatedStatus() throws Exception {
        CreateAdmin createAdmin = CreateAdmin.builder()
                .email("admin@mail.com")
                .name("Admin")
                .surname("Adminovich")
                .password("super_secret")
                .build();

        doNothing().when(adminService).createAdmin(any(CreateAdmin.class));

        mockMvc.perform(post("/admin/create")
                        .content(objectMapper.writeValueAsString(createAdmin))
                        .with(csrf())
                        .characterEncoding("utf-8")
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(adminService).createAdmin(any(CreateAdmin.class));
    }

    @Test
    public void givenDeleteProof_whenValidProofId_thenDeletesProof() throws Exception {
        Long proofId = 2L;

        mockMvc.perform(delete("/admin/proofs/{proof-id}", proofId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(adminService).deleteProof(proofId);
    }

    @Test
    public void givenEditKind_whenValidKindIdAndRequestBody_thenReturnsKindDTO() throws Exception {
        Long kindId = 3L;
        String newKind = "Qwerty";
        KindDTO expectedKindDTO = new KindDTO(kindId, newKind);

        when(adminService.editKind(kindId, newKind)).thenReturn(expectedKindDTO);

        mockMvc.perform(patch("/admin/talents/kinds/{kind-id}", kindId)
                        .content(newKind)
                        .with(csrf())
                        .characterEncoding("utf-8")
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedKindDTO)))
                .andDo(print());

        verify(adminService).editKind(kindId, newKind);
    }

    @Test
    public void givenAddSkill_whenValidRequestBody_thenAddsSkill() throws Exception {
        Long skillId = 10L;
        SkillDTO skillDTO = SkillDTO.builder()
                .id(skillId)
                .label("Qwerty")
                .icon("icon.svg")
                .build();

        mockMvc.perform(post("/admin/skills")
                        .content(objectMapper.writeValueAsString(skillDTO))
                        .with(csrf())
                        .characterEncoding("utf-8")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(adminService).addSkill(skillDTO);
    }

    @Test
    public void givenDeleteSkill_whenValidSkillId_thenDeletesSkill() throws Exception {
        Long skillId = 10L;

        mockMvc.perform(delete("/admin/skills/{skill-id}", skillId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(adminService).deleteSkill(skillId);
    }
}

