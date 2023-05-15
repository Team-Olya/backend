package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.admin.repository.AdminRepository;
import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.KindDTO;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final JwtEncoder jwtEncoder;
    final PasswordEncoder passwordEncoder;
    AdminRepository adminRepository;
    TalentRepository talentRepository;
    LinkRepository linkRepository;
    ProofRepository proofRepository;
    KindRepository kindRepository;
    SkillRepository skillRepository;
    TalentMapper talentMapper;
    ProofMapper proofMapper;

    @Override
    public void deleteTalent(String email) {
        var talent = talentRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent doesn't exist")
        );
        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
    }

    @Override
    public void deleteTalent(Long talentId) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent doesn't exist")
        );
        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
    }

    @Override
    public void deleteProof(Long proofId) {
        proofRepository.deleteById(proofId);
    }

    @Override
    public UpdatedTalentResponse editTalent(TalentUpdateRequest newTalent, Long talentId) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));
        return updateTalent(talent, newTalent);
    }

    @Override
    public ProofDTO editProof(ProofRequest newProof, Long proofId) {
        var proof = proofRepository.findById(proofId).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proof with ID" + proofId + " not found!")
        );
        Optional.ofNullable(newProof.title()).ifPresent(proof::setTitle);
        Optional.ofNullable(newProof.description()).ifPresent(proof::setDescription);
        Optional.ofNullable(newProof.status()).ifPresent(proof::setStatus);
        if (newProof.skills() != null) {
            List<Skill> allSkills = skillRepository.findAll();
            List<Skill> skills = allSkills.stream()
                    .filter(skill -> newProof.skills().contains(skill.getLabel()))
                    .collect(Collectors.toList());

            Optional.of(skills).ifPresent(proof::setSkills);
        }
        proofRepository.save(proof);
        return proofMapper.toProofDTO(proof);
    }

    @Override
    public KindDTO editKind(Long id, String newKind) {
        var kind = kindRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Kind with ID " + id + " doesn't exist")
        );
        kind.setKind(newKind);
        kindRepository.save(kind);
        return new KindDTO(kind.getId(), kind.getKind());
    }

    @Override
    public void addSkill(SkillDTO skillDTO) {
        skillRepository.save(Skill.builder()
                .icon(skillDTO.icon())
                .label(skillDTO.label())
                .build()
        );
    }

    @Override
    public void deleteSkill(Long skillId) {
        skillRepository.deleteById(skillId);
    }

    @Override
    public void createAdmin(CreateAdmin createAdmin) {
        if (adminRepository.count() == 0) {
            adminRepository.save(Admin.builder()
                    .email(createAdmin.email())
                    .password(passwordEncoder.encode(createAdmin.password()))
                    .name(createAdmin.name())
                    .surname(createAdmin.surname())
                    .authorities(List.of(Roles.ADMIN.name()))
                    .build()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create admin account, idiot hacker");
        }
    }

    public String login(Authentication authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        var user = adminRepository.findByEmailIgnoreCase(authentication.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong authentication data!"));
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private UpdatedTalentResponse updateTalent(Talent oldTalent, TalentUpdateRequest newTalent) {
        Optional.ofNullable(newTalent.name()).ifPresent(oldTalent::setName);
        Optional.ofNullable(newTalent.surname()).ifPresent(oldTalent::setSurname);
        Optional.ofNullable(newTalent.kind())
                .ifPresent(kind -> {
                    if (!kindRepository.existsByKindIgnoreCase(kind)) {
                        Kind newKind = Kind.builder()
                                .kind(kind)
                                .build();
                        kindRepository.save(newKind);
                        oldTalent.setKind(newKind);
                    } else {
                        oldTalent.setKind(kindRepository.findByKindIgnoreCase(kind));
                    }
                });
        Optional.ofNullable(newTalent.avatar()).ifPresent(oldTalent::setAvatar);
        Optional.ofNullable(newTalent.description()).ifPresent(oldTalent::setDescription);
        Optional.ofNullable(newTalent.experience()).ifPresent(oldTalent::setExperience);
        Optional.ofNullable(newTalent.location()).ifPresent(oldTalent::setLocation);
        Optional.ofNullable(newTalent.links()).ifPresent(links -> {
            linkRepository.deleteByTalent(oldTalent);
            List<Link> newLinks = links.stream()
                    .map(url -> Link.builder()
                            .url(url)
                            .talent(oldTalent)
                            .build())
                    .map(linkRepository::save)
                    .collect(Collectors.toList());
            oldTalent.setLinks(newLinks);
        });
        if (newTalent.skills() != null) {
            List<Skill> allSkills = skillRepository.findAll(); //TODO: ignore case
            List<Skill> skills = allSkills.stream()
                    .filter(skill -> newTalent.skills().contains(skill.getLabel()))
                    .collect(Collectors.toList());

            Optional.of(skills).ifPresent(oldTalent::setSkills);
        }
        talentRepository.save(oldTalent);
        return talentMapper.toUpdatedTalent(oldTalent);
    }
}
