package com.teamolha.talantino.account.mapper;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.admin.model.AccountStatus;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AccountMapper {

    default UserDetails toUserDetails(Account account) {
        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .authorities(account.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                )
                .disabled(AccountStatus.INACTIVE.equals(account.getAccountStatus()))
                .build();
    }

    @Mapping(target = "authorities", ignore = true)
    Talent accountToTalent(Account account);

    @Mapping(target = "authorities", ignore = true)
    Sponsor accountToSponsor(Account account);

    @Mapping(target = "authorities", ignore = true)
    Sponsor accountToAdmin(Account account);
}
