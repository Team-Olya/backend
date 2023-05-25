//package com.teamolha.talantino.unit.proof;
//
//import com.teamolha.talantino.proof.controller.ProofController;
//import com.teamolha.talantino.proof.service.ProofService;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = ProofController.class)
//@Disabled
//public class ProofControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProofService proofService;
//
//    @Test
//    public void whenValidUrlAndMethodAndContentType_thenReturns200() throws Exception {
//        mockMvc.perform(get("/proofs").contentType("application/json")).andExpect(status().isOk());
//    }
//}
