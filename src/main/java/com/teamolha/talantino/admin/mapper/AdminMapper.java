package com.teamolha.talantino.admin.mapper;

import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.admin.model.response.AdminProfile;
import com.teamolha.talantino.general.config.Roles;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AdminMapper {
    default AdminProfile toAdminProfile(Admin admin){
        return AdminProfile.builder()
                .role(Roles.ADMIN.name())
                .id(admin.getId())
                .surname(admin.getSurname())
                .name(admin.getName())
                .build();
    };
}
