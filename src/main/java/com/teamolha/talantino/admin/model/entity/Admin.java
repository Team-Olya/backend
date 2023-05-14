package com.teamolha.talantino.admin.model.entity;

import com.teamolha.talantino.account.model.entity.Account;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Admin extends Account {
    @Override
    public String getAvatar() {
        return null;
    }
}
