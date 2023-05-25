package com.teamolha.talantino;

import com.teamolha.talantino.account.controller.AuthController;
import com.teamolha.talantino.proof.controller.ProofController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TalantinoApplicationTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private ProofController proofController;

    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(proofController).isNotNull();
    }

}
