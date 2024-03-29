package com.teamolha.talantino.sponsor.service.impl;

import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.general.email.EmailHelper;
import com.teamolha.talantino.general.email.EmailSender;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.model.entity.BalanceChanging;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.BalanceChangingDTO;
import com.teamolha.talantino.sponsor.model.response.BalanceHistoryDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import com.teamolha.talantino.sponsor.repository.BalanceChangingRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private BalanceChangingRepository balanceChangingRepository;
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
        balanceChangingRepository.save(BalanceChanging.builder()
                .amount(addKudosRequest.amount())
                .sponsorId(sponsor.getId())
                .date(LocalDateTime.now(ZoneOffset.UTC))
                .build());
        return mapper.toSponsorProfileResponse(sponsorRepository.save(sponsor));
    }

    @Override
    public BalanceHistoryDTO getBalanceHistory(Authentication auth, int page, int size) {
        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor is not found")
        );
        if (size <= 0 || page < 0) {
            return BalanceHistoryDTO.builder()
                    .totalAmount(balanceChangingRepository.countBySponsorId(sponsor.getId()))
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        var balanceChangings = balanceChangingRepository.findBySponsorId(sponsor.getId(), pageable)
                .stream()
                .map(balanceChanging -> BalanceChangingDTO.builder()
                        .amount(balanceChanging.getAmount())
                        .date(balanceChanging.getDate())
                        .talentId(balanceChanging.getTalent() == null ? null : balanceChanging.getTalent().getId())
                        .build())
                .collect(Collectors.toList());
        return BalanceHistoryDTO.builder()
                .totalAmount(balanceChangingRepository.countBySponsorId(sponsor.getId()))
                .balanceChangings(balanceChangings)
                .build();
    }

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
        sponsor.setDeletionDate(emailHelper.calculateDeletionDate());
        sponsorRepository.save(sponsor);
        emailSender.deactivateAccount(request, sponsor.getEmail(), sponsor.getDeletionToken());
    }

    private UpdatedSponsorResponse updateSponsor(Sponsor oldSponsor, SponsorUpdateRequest newSponsor) {
        Optional.ofNullable(newSponsor.name()).ifPresent(oldSponsor::setName);
        Optional.ofNullable(newSponsor.surname()).ifPresent(oldSponsor::setSurname);
        Optional.ofNullable(newSponsor.avatar()).ifPresent(oldSponsor::setAvatar);
        sponsorRepository.save(oldSponsor);
        return mapper.toUpdatedSponsor(oldSponsor);
    }
}
