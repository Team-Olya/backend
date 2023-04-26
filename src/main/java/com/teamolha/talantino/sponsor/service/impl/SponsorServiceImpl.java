package com.teamolha.talantino.sponsor.service.impl;

import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class SponsorServiceImpl implements SponsorService {
    private PasswordEncoder encoder;
    private SponsorMapper mapper;
    private TalentRepository talentRepository;
    private SponsorRepository sponsorRepository;

    @Override
    public void register(String email, String password, String name, String surname) {
        if (talentRepository.existsByEmailIgnoreCase(email) || sponsorRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    email + " is already occupied!"
            );
        }
        sponsorRepository.save(
                Sponsor.builder()
                        .email(email)
                        .password(encoder.encode(password))
                        .name(name)
                        .surname(surname)
                        .balance(0L)
                        .authorities(List.of(Roles.SPONSOR.name()))
                        .build()
        );
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
        return mapper.toSponsorProfileResponse(sponsorRepository.save(sponsor));
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

    private UpdatedSponsorResponse updateSponsor(Sponsor oldSponsor, SponsorUpdateRequest newSponsor) {
        Optional.ofNullable(newSponsor.name()).ifPresent(oldSponsor::setName);
        Optional.ofNullable(newSponsor.surname()).ifPresent(oldSponsor::setSurname);
        Optional.ofNullable(newSponsor.balance()).ifPresent(oldSponsor::setBalance);
        Optional.ofNullable(newSponsor.avatar()).ifPresent(oldSponsor::setAvatar);
        sponsorRepository.save(oldSponsor);
        return mapper.toUpdatedSponsor(oldSponsor);
    }
}
