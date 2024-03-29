package com.teamolha.talantino.talent.service.impl;

import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.account.repository.AccountRepository;
import com.teamolha.talantino.general.discord.event.MessageSendEvent;
import com.teamolha.talantino.general.email.EmailHelper;
import com.teamolha.talantino.general.email.EmailSender;
import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.skill.mapper.SkillMapper;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.FullSkillDTO;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.ReportTalent;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.*;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.ReportTalentRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import com.teamolha.talantino.talent.service.TalentService;
import discord4j.rest.http.client.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {

    private ReportTalentRepository reportTalentRepository;

    private TalentMapper mapper;

    private ProofMapper proofMapper;

    private SkillMapper skillMapper;

    private LinkRepository linkRepository;

    private TalentRepository talentRepository;

    private KindRepository kindRepository;

    private ProofRepository proofRepository;

    private SkillRepository skillRepository;

    private SponsorRepository sponsorRepository;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private MessageSendEvent messageSendEvent;

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

        if (!talent.getEmail().equals(email) || talent.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return updateTalent(talent, updateTalent);
    }

    @Override
    public void deleteTalent(HttpServletRequest request, long talentId, String email) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));

        if (!email.equals(talent.getEmail()) || talent.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        talent.setAccountStatus(AccountStatus.INACTIVE);
        talent.setDeletionToken(UUID.randomUUID().toString());
        talent.setDeletionDate(emailHelper.calculateDeletionDate());
        talentRepository.save(talent);
        emailSender.deactivateAccount(request, talent.getEmail(), talent.getDeletionToken());
    }

    @Override
    public KindsDTO getTalentKinds(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        var amount = kindRepository.count();

        return new KindsDTO(amount, kindRepository.findAll(pageable)
                .stream()
                .map(kind -> new KindDTO(kind.getId(), kind.getKind()))
                .collect(Collectors.toList())
        );
    }

    @Override
    public TalentStatistic getStatistic(long talentId, String email) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        var queryResultForProof = talentRepository.findMostKudosedProof(talentId);
        Long mostKudosedProofId = !queryResultForProof.isEmpty() ? (Long) queryResultForProof.get(0)[0] : null;
        var mostKudosedProof = mostKudosedProofId != null ? proofRepository.findById(mostKudosedProofId).orElse(null) : null;

        var queryResultForSkill = talentRepository.findMostKudosedSkill(talentId);
        Long mostKudosedSkillId = !queryResultForSkill.isEmpty() ? (Long) queryResultForSkill.get(0)[0] : null;
        var mostKudosedSkill = mostKudosedSkillId != null ? skillRepository.findById(mostKudosedSkillId).orElse(null) : null;
        var totalKudos = !queryResultForSkill.isEmpty() ? (Long) queryResultForSkill.get(0)[1] : null;

        return TalentStatistic.builder()
                .totalAmount(talent.getProofs().stream().map(proof -> proof.getKudos().size()).count())
                .skill(mostKudosedSkill != null
                        ? FullSkillDTO.builder().id(mostKudosedSkill.getId())
                        .label(mostKudosedSkill.getLabel())
                        .icon(mostKudosedSkill.getIcon())
                        .totalKudos(totalKudos).build()
                        : null)
                .proof(mostKudosedProof != null
                        ? proofMapper.toProofDTO(mostKudosedProof, skillMapper, true)
                        : null)
                .build();
    }

    @Override
    public ReportedTalentDTO reportTalent(Authentication auth, Long talentId, HttpServletRequest request) {
        var account = accountRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));
        if (talent.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot report a talent whose account is inactive");
        }
        if (talent.getId().equals(account.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot report yourself");
        }

        ReportTalent reportTalent = reportTalentRepository.save(ReportTalent.builder().account(account).talent(talent).build());

        ReportedTalentDTO reportedTalent = mapper.toReportTalentDTO(reportTalent.getId(), talent, account);

        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = request.getScheme() + "://" + request.getHeader("host") + "/";
        }

        sendReportTalentMessage(reportedTalent, referer);
        return reportedTalent;
    }

    @Override
    public void approveReport(Long reportId) {
        var report = reportTalentRepository.findById(reportId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        reportTalentRepository.deleteById(reportId);
        var talent = report.getTalent();
        if (!talentRepository.existsById(talent.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        talent.setAccountStatus(AccountStatus.INACTIVE);
        talentRepository.save(talent);
    }

    @Override
    public void rejectReport(Long reportId) {

        reportTalentRepository.deleteById(reportId);
    }

    private void sendReportTalentMessage(ReportedTalentDTO reportedTalent, String referer) {
        try {
            messageSendEvent.sendReportTalentMessage(reportedTalent, referer);
        } catch (ClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Moderator Bot exception");
        }
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

