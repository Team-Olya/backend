package com.teamolha.talantino.talent.service.impl;

import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.general.email.EmailHelper;
import com.teamolha.talantino.general.email.EmailSender;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.*;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import com.teamolha.talantino.talent.service.TalentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {

    private TalentMapper mapper;

    private LinkRepository linkRepository;

    private TalentRepository talentRepository;

    private KindRepository kindRepository;

    private ProofRepository proofRepository;

    private SkillRepository skillRepository;

    private SponsorRepository sponsorRepository;

    private PasswordEncoder passwordEncoder;

    private EmailHelper emailHelper;

    private EmailSender emailSender;

    @Override
    public void register(String email, String password, String name, String surname, String kind,
                         HttpServletRequest request) {
        if (talentRepository.existsByEmailIgnoreCase(email) || sponsorRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    email + " is already occupied!"
            );
        }
        if (!kindRepository.existsByKindIgnoreCase(kind)) {
            kindRepository.save(
                    Kind.builder()
                            .kind(kind)
                            .build()
            );
        }
        String token = emailHelper.generateUUIDToken();
        talentRepository.save(
                Talent.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .name(name)
                        .surname(surname)
                        .kind(kindRepository.findByKindIgnoreCase(kind))
                        .authorities(List.of(AccountRole.TALENT.name()))
                        .accountStatus(AccountStatus.INACTIVE)
                        .verificationExpireDate(emailHelper.calculateExpireVerificationDate())
                        .verificationToken(token)
                        .build()
        );
        emailSender.verificationAccount(request, email, token);
    }

    @Override
    public TalentProfileResponse talentProfile(String email) {
        var talent = talentRepository.findByEmailIgnoreCase(email).get();

        return mapper.toTalentProfileResponse(
                talent, getPrevTalentId(talent.getId()), getNextTalentId(talent.getId())
        );
    }

    @Transactional(readOnly = true)
    @Override
    public TalentsPageResponse pageTalents(int page, int size, String skills) {
        if (size <= 0) {
            return TalentsPageResponse.builder()
                    .totalAmount(talentRepository.findByAccountStatusOrAccountStatusIsNull(AccountStatus.ACTIVE).size())
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size);

        List<Talent> talents;
        long amount;
        if (skills == null) {
            talents = talentRepository.findByAccountStatusOrAccountStatusIsNullOrderByIdDesc(pageable, AccountStatus.ACTIVE);
            amount = talentRepository.findByAccountStatusOrAccountStatusIsNull(AccountStatus.ACTIVE).size();
        } else {
            var skillList = getSkillList(skills);
            talents = talentRepository.findAllBySkills(skillList, (long) skillList.size(), pageable);
            amount = talentRepository.findAllBySkills(skillList, (long) skillList.size()).size();
        }

        return TalentsPageResponse.builder()
                .totalAmount(amount)
                .talents(talents.stream()
                        .map(TalentGeneralResponse::new)
                        .toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TalentFullResponse talentProfile(long talentId) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));

        return mapper.toTalentFullResponse(talent, getPrevTalentId(talentId), getNextTalentId(talentId));
    }

    @Override
    public UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));

        if (!talent.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return updateTalent(talent, updateTalent);
    }

    @Override
    public void deleteTalent(long talentId, String email) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
    }

    @Override
    public List<KindDTO> getTalentKinds() {
        return kindRepository.findAll()
                .stream()
                .map(kind -> new KindDTO(kind.getId(), kind.getKind()))
                .collect(Collectors.toList());
    }

    private Long getPrevTalentId(long talentId) {
        return talentRepository.findPrevTalent(talentId);
    }

    private Long getNextTalentId(long talentId) {
        return talentRepository.findNextTalent(talentId);
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
        return mapper.toUpdatedTalent(oldTalent);
    }

    private List<Skill> getSkillList(String skillsString) {
        List<String> labels = List.of(skillsString.split(","));
        List<Skill> skillList = new ArrayList<>();
        labels.forEach(label ->
                skillRepository.findByLabelIgnoreCase(label)
                        .ifPresent(skillList::add)
        );
        return skillList;
    }

}

