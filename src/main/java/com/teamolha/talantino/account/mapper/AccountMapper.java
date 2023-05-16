package com.teamolha.talantino.account.mapper;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.account.model.AccountStatus;
import org.mapstruct.Mapper;
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
}
