package com.teamolha.talantino.talent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.TalentGeneralResponse;
import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.model.response.TalentsPageResponse;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class TalentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListTalents() throws Exception {
        TalentsPageResponse expectedResponse = TalentsPageResponse.builder().build();
        expectedResponse.setTotalAmount(11);

        List<TalentGeneralResponse> talents = new ArrayList<>();
        talents.add(new TalentGeneralResponse(6L, "Bohdan", "Rohozianskyi", "JS Developer", null,
                Collections.singletonList(new SkillDTO("Skill 1", "iconurl"))));
        talents.add(new TalentGeneralResponse(5L, "Max", "Koropets", "QA", null,
                Collections.singletonList(new SkillDTO("Skill 1", "iconurl"))));
        talents.add(new TalentGeneralResponse(4L, "Anastasiia", "Mashchenko", "QA", null,
                Collections.singletonList(new SkillDTO("Skill 1", "iconurl"))));
        talents.add(new TalentGeneralResponse(3L, "Ekaterina", "Nikitenko", "QA", null,
                Collections.singletonList(new SkillDTO("Skill 1", "iconurl"))));
        talents.add(new TalentGeneralResponse(2L, "Alexey", "Pedun", "QA", null,
                Collections.singletonList(new SkillDTO("Skill 1", "iconurl"))));

        expectedResponse.setTalents(talents);

        this.mockMvc.perform(get("/talents")
                        .param("amount", "5")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testTalentProfileWithAuthorization() throws Exception {
        TalentProfileResponse expectedResponse = TalentProfileResponse.builder()
                .id(10L)
                .name("Yaroslava")
                .surname("Nechaieva")
                .email("yn@mail.com")
                .kind("Java Developer")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industrys standard dummy text ever since the 1500s," +
                        " when an unknown printer took a galley of type and scrambled it to make a type specimen book.")
                .avatar(null)
                .experience(5)
                .location("Ukraine")
                .links(List.of("https://dota2.com", "https://blizzard.com"))
                .prevId(9L)
                .nextId(11L)
                .build();
        mockMvc.perform(get("/talents/10"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    public void testTalentProfileWithoutAuthorization() throws Exception {
        mockMvc.perform(get("/talents/10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testTalentProfileNotFound() throws Exception {
        mockMvc.perform(get("/talents/120"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "vl@mail.com")
    public void testUpdateTalentProfileForbidden() throws Exception {
        TalentUpdateRequest request = TalentUpdateRequest.builder()
                .name("Mark")
                .surname("Severov")
                .kind("dota 2 player")
                .avatar(null)
                .description("I'm best player")
                .experience(100)
                .location("Pavlohrad")
                .links(List.of("dota2.com"))
                .build();

        mockMvc.perform(patch("/talents/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "vl@mail.com")
    public void testUpdateTalentProfile() throws Exception {
        TalentUpdateRequest request = TalentUpdateRequest.builder()
                .name("Mark")
                .surname("Severov")
                .kind("dota 2 player")
                .avatar(null)
                .description("I'm best player")
                .experience(100)
                .location("Pavlohrad")
                .links(List.of("dota2.com"))
                .build();

        UpdatedTalentResponse response = UpdatedTalentResponse.builder()
                .id(9L)
                .name("Mark")
                .surname("Severov")
                .kind("dota 2 player")
                .avatar(null)
                .description("I'm best player")
                .experience(100)
                .location("Pavlohrad")
                .links(List.of("dota2.com"))
                .build();

        mockMvc.perform(patch("/talents/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    @WithMockUser(username = "vl@mail.com")
    public void testDeleteTalentForbidden() throws Exception {
        mockMvc.perform(delete("/talents/5"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "vl@mail.com")
    public void testDeleteTalent() throws Exception {
        mockMvc.perform(delete("/talents/9"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yn@mail.com")
    public void testFindTalentAfterDeletion() throws Exception {
        mockMvc.perform(get("/talents/9"))
                .andExpect(status().isNotFound());
    }
}
