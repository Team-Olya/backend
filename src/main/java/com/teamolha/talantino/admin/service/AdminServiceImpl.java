package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.account.repository.AccountRepository;
import com.teamolha.talantino.admin.mapper.AdminMapper;
import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.admin.model.response.*;
import com.teamolha.talantino.admin.repository.AdminRepository;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.notification.repository.KudosNotificationRepository;
import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.ShortProofDTO;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.skill.mapper.SkillMapper;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.skill.repository.SkillRepository;
import com.teamolha.talantino.sponsor.repository.BalanceChangingRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.mapper.TalentMapper;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.*;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final KudosNotificationRepository notificationRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final TalentRepository talentRepository;
    private final LinkRepository linkRepository;
    private final ProofRepository proofRepository;
    private final KindRepository kindRepository;
    private final SkillRepository skillRepository;
    private final AdminMapper adminMapper;
    private final SponsorRepository sponsorRepository;
    private final BalanceChangingRepository balanceChangingRepository;

    @Override
    public void deleteTalent(Long talentId) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent doesn't exist")
        );
        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        notificationRepository.deleteByToTalentId(talentId);
        var balanceChanging = balanceChangingRepository.findAllByTalent(talent);
        balanceChanging.forEach(changing -> {
            changing.setTalent(null);
            balanceChangingRepository.save(changing);
        });
        talentRepository.delete(talent);
    }

    @Override
    public void deleteProof(Long proofId) {
        notificationRepository.deleteByProofId(proofId);
        proofRepository.deleteById(proofId);
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
    public AdminTalentDTO getTalent(Long talentId) {
        return adminMapper.toAdminTalentDTO(talentRepository.findById(talentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with id " + talentId + " not found")
                )
        );
    }

    @Override
    public AdminTalentsDTO getTalents(String email, int page, int size) {
        if (size <= 0) {
            return AdminTalentsDTO.builder()
                    .amount(talentRepository.countByEmailStartsWithIgnoreCase(email))
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size);

        List<Talent> talents;
        long amount;
        talents = talentRepository.findByEmailStartsWithIgnoreCase(email, pageable);
        amount = talentRepository.countByEmailStartsWithIgnoreCase(email);


        return AdminTalentsDTO.builder()
                .amount(amount)
                .talents(talents.stream()
                        .map(adminMapper::toAdminTalentDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public AdminProofsDTO getProofs(String title, int page, int size) {
        long totalAmount = proofRepository.countByTitleStartsWithIgnoreCase(title);

        if (size <= 0 || page < 0) {
            return AdminProofsDTO.builder()
                    .amount(totalAmount)
                    .build();
        }

        Pageable pageable = PageRequest.of(page, size);

        var proofs = proofRepository.findByTitleStartsWithIgnoreCase(title, pageable);

        return AdminProofsDTO.builder()
                .amount(totalAmount)
                .proofs(proofs.stream()
                        .map(adminMapper::toAdminProofDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public AdminProofDTO getProof(Long proofId) {
        return adminMapper.toAdminProofDTO(proofRepository.findById(proofId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Proof with ID" + proofId + " doesn't exist")
                )
        );
    }

    @Override
    public AdminSponsorsDTO getSponsors(String email, int page, int size) {
        long totalAmount = sponsorRepository.countByEmailStartsWithIgnoreCase(email);

        if (size <= 0 || page < 0) {
            return AdminSponsorsDTO.builder()
                    .amount(totalAmount)
                    .build();
        }

        Pageable pageable = PageRequest.of(page, size);

        var sponsors = sponsorRepository.findByEmailStartsWithIgnoreCase(email, pageable);

        return AdminSponsorsDTO.builder()
                .amount(totalAmount)
                .sponsors(sponsors.stream()
                        .map(adminMapper::toAdminSponsorDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public AdminSponsorDTO getSponsor(Long sponsorId) {
        return adminMapper.toAdminSponsorDTO(sponsorRepository.findById(sponsorId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Sponsor with ID " + sponsorId + " not found")
                )
        );
    }

    @Override
    public void unbannedAccount(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account is already active");
        }
        account.setAccountStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Override
    public void createAdmin(CreateAdmin createAdmin) {
        if (adminRepository.count() == 0) {
            adminRepository.save(Admin.builder()
                    .email(createAdmin.email())
                    .password(passwordEncoder.encode(createAdmin.password()))
                    .name(createAdmin.name())
                    .surname(createAdmin.surname())
                    .authorities(List.of(AccountRole.ADMIN.name()))
                    .build()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create admin account, idiot hacker");
        }
    }
}
