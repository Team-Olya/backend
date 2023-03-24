package com.teamolha.talantino.mapper;

import com.teamolha.talantino.model.entity.Talent;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface Mappers {

    default UserDetails toUserDetails(Talent talent) {
        return User.withUsername(talent.getEmail())
                .password(talent.getPassword())
                .authorities(talent.getAuthorities().toArray(String[]::new))
                .build();
    }
}
