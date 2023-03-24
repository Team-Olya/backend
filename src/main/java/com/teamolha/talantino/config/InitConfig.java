//package com.teamolha.talantino.config;
//
//import com.teamolha.talantino.model.entity.Kind;
//import com.teamolha.talantino.model.entity.Talent;
//import com.teamolha.talantino.repository.TalentRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@AllArgsConstructor
//public class InitConfig implements CommandLineRunner {
//
////    TalentRepository talentRepository;
////
////    @Override
////    public void run(String... args) throws Exception {
//////        for (int i = 0; i < 20; i++) {
//////            Talent talent = Talent.builder()
//////                    .password("$2a$12$ngG1dPTpSEwbX.n99q2hKeatPahmBJnmzq9hTq.m5G8PMYeK3hKcC")
//////                    .email("johndoe" + i +"@mail.com")
//////                    .kind(Kind.builder().kind("Developer" + i).build())
//////                    .name("John")
//////                    .surname("Doe" + i)
//////                    .build();
//////            talentRepository.save(talent);
//////        }
////    }
//}
