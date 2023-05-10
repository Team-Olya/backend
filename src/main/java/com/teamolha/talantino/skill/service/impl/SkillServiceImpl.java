package com.teamolha.talantino.skill.service.impl;


import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.skill.model.response.SkillListDTO;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.skill.service.SkillService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {

    SkillRepository skillRepository;

    @Override
    public SkillListDTO getSkillList(String search) {
        List<Skill> skills = (search == null) ?
                skillRepository.findAll() :
                skillRepository.findSkillsStartingWith(search);

        return SkillListDTO.builder()
                .skills(skills.stream()
                        .map(SkillDTO::new)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public SkillListDTO getProofSkills(Long proofId) {
        return SkillListDTO.builder()
                .skills(skillRepository.findByProofs_Id(proofId)
                        .stream()
                        .map(SkillDTO::new)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
