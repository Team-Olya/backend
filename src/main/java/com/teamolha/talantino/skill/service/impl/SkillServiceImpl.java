package com.teamolha.talantino.skill.service.impl;


import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.repository.KudosRepository;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.skill.mapper.SkillMapper;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.skill.model.response.SkillListDTO;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.skill.service.SkillService;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {

    private SkillRepository skillRepository;

    private SkillMapper skillMapper;

    private SponsorRepository sponsorRepository;

    private ProofRepository proofRepository;

    private KudosRepository kudosRepository;

    @Override
    public SkillListDTO getSkillList(String search) {
        List<Skill> skills = (search == null) ?
                skillRepository.findAll() :
                skillRepository.findSkillsStartingWith(search);

        return SkillListDTO.builder()
                .skills(skills.stream()
                        .map(skillMapper::toSkillDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public SkillListDTO getProofSkills(Long proofId) {
        return SkillListDTO.builder()
                .skills(skillRepository.findByProofs_Id(proofId)
                        .stream()
                        .map(skillMapper::toSkillDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public void setKudosToSkill(Authentication auth, Long proofId, Long skillId, int amount) {
        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Only sponsors have access to kudos"));
        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must bet at least 1 kudos");
        }
        if (sponsor.getBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough totalKudos");
        }

        var proof = getProofEntity(proofId);

        var skill = skillRepository.findById(skillId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill with ID " + skillId + " does not exist"));
        if (!proof.getStatus().equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only put kudos in published proofs");
        }
        if (!proof.getSkills().contains(skill)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't put kudos on proofs without skills");
        }

        List<Kudos> sponsorKudos = sponsor.getKudos();
        if (!kudosRepository.existsBySponsorIdAndProofIdAndSkillId(sponsor.getId(), proofId, skillId)) {
            sponsorKudos.add(Kudos.builder()
                    .amount(amount)
                    .sponsorId(sponsor.getId())
                    .proofId(proofId)
                    .skillId(skillId)
                    .build());
        } else {
            sponsorKudos.stream().filter(kudos -> kudos.getProofId().equals(proofId) &&
                            (kudos.getSkillId() != null && kudos.getSkillId().equals(skillId)))
                    .forEach(kudos -> kudos.setAmount(kudos.getAmount() + amount));
        }
        sponsor.setKudos(sponsorKudos);
        sponsor.setBalance(sponsor.getBalance() - amount);
        sponsorRepository.save(sponsor);
    }

    private Proof getProofEntity(Long proofId) {
        return proofRepository.findById(proofId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proof with ID " + proofId + " not found"));
    }
}
