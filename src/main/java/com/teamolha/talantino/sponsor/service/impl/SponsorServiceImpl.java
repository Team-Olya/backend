package com.teamolha.talantino.sponsor.service.impl;

import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.model.entity.AccountStatus;
import com.teamolha.talantino.general.config.EmailProperties;
import com.teamolha.talantino.general.email.EmailHelper;
import com.teamolha.talantino.general.email.EmailSender;
import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.model.entity.BalanceAdding;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.BalanceAddingDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorKudos;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import com.teamolha.talantino.sponsor.repository.BalanceAddingRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SponsorServiceImpl implements SponsorService {
    private PasswordEncoder encoder;
    private SponsorMapper mapper;
    private TalentRepository talentRepository;
    private SponsorRepository sponsorRepository;
    private EmailSender emailSender;
    private BalanceAddingRepository balanceAddingRepository;
    private ProofMapper proofMapper;
    private ProofRepository proofRepository;
    private EmailHelper emailHelper;

    @Override
    public void register(String email, String password, String name, String surname, HttpServletRequest request) {
        if (talentRepository.existsByEmailIgnoreCase(email) || sponsorRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    email + " is already occupied!"
            );
        }
        String token = emailHelper.generateUUIDToken();
        sponsorRepository.save(
                Sponsor.builder()
                        .email(email)
                        .password(encoder.encode(password))
                        .name(name)
                        .surname(surname)
                        .balance(0L)
                        .accountStatus(AccountStatus.INACTIVE)
                        .verificationExpireDate(emailHelper.calculateExpireVerificationDate())
                        .verificationToken(token)
                        .authorities(List.of(AccountRole.SPONSOR.name()))
                        .build()
        );
        emailSender.verificationAccount(request, email, token);
    }

    @Override
    public SponsorProfileResponse addKudos(Authentication auth, AddKudosRequest addKudosRequest) {
        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Only sponsor can add funds"));
        if (addKudosRequest.amount() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can add a maximum of 1000 per transaction");
        } else if (addKudosRequest.amount() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "We can't pay to your card, sorry");
        }
        sponsor.setBalance(sponsor.getBalance() + addKudosRequest.amount());
        balanceAddingRepository.save(BalanceAdding.builder()
                .amount(addKudosRequest.amount())
                .sponsorId(sponsor.getId())
                .date(LocalDateTime.now(ZoneOffset.UTC))
                .build());
        return mapper.toSponsorProfileResponse(sponsorRepository.save(sponsor));
    }

    @Override
    public List<BalanceAddingDTO> getBalanceAddingHistory(Authentication auth) {
        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor is not found")
        );
        return balanceAddingRepository.findAllBySponsorId(sponsor.getId())
                .stream()
                .map(balanceAdding -> BalanceAddingDTO.builder()
                        .amount(balanceAdding.getAmount())
                        .date(balanceAdding.getDate())
                        .build())
                .collect(Collectors.toList());
    }

//    @Override
//    public List<SponsorKudos> getKudosHistory(Authentication auth) {
//        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow( () ->
//                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor is not found")
//        );
//        return sponsor.getKudos().stream()
//                .map(kudos -> SponsorKudos.builder()
//                        .proofDTO(proofMapper.toProofDTO(proofRepository.findById(kudos.getProofId()).orElseThrow(), sponsor))
//                        .amount(kudos.getAmount())
//                        .build())
//                .collect(Collectors.toList());
//    }

    @Override
    public UpdatedSponsorResponse updateSponsorProfile(long sponsorId, String email, SponsorUpdateRequest updateSponsor) {
        var sponsor = sponsorRepository.findById(sponsorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sponsor with ID " + sponsorId + " not found!"));

        if (!sponsor.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return updateSponsor(sponsor, updateSponsor);
    }

    @Override
    public void deleteSponsor(HttpServletRequest request, Authentication auth, long sponsorId) {
        var sponsor = sponsorRepository.findById(sponsorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sponsor with ID " + sponsorId + " not found"));

        if (!auth.getName().equals(sponsor.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        sponsor.setAccountStatus(AccountStatus.INACTIVE);
        sponsor.setDeletionToken(UUID.randomUUID().toString());
        sponsor.setDeletionDate(calculateDeletionDate(7));
        sponsorRepository.save(sponsor);
        emailSender.deactivateAccount(request, sponsor.getEmail(), sponsor.getDeletionToken());
    }

    @Override
    public void recoverSponsor(String token) {
        var sponsor = sponsorRepository.findByDeletionToken(token).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token"));
        sponsor.setAccountStatus(AccountStatus.ACTIVE);
        sponsor.setDeletionDate(null);
        sponsor.setDeletionToken(null);
        sponsorRepository.save(sponsor);
    }

    private UpdatedSponsorResponse updateSponsor(Sponsor oldSponsor, SponsorUpdateRequest newSponsor) {
        Optional.ofNullable(newSponsor.name()).ifPresent(oldSponsor::setName);
        Optional.ofNullable(newSponsor.surname()).ifPresent(oldSponsor::setSurname);
        Optional.ofNullable(newSponsor.avatar()).ifPresent(oldSponsor::setAvatar);
        sponsorRepository.save(oldSponsor);
        return mapper.toUpdatedSponsor(oldSponsor);
    }

    private Date calculateDeletionDate(int deletionDateInDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.DAY_OF_WEEK, deletionDateInDays);
        return new Date(cal.getTime().getTime());
    }
}
