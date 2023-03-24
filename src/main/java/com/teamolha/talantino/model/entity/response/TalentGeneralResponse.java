package com.teamolha.talantino.model.entity.response;

import com.teamolha.talantino.model.entity.Talent;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class TalentGeneralResponse {
    long id;
    String name;
    String surname;

    String kindOfTalent;
    String profilePicture;

    public TalentGeneralResponse(Talent talent){
        this.id = talent.getId();
        this.name = talent.getName();
        this.surname = talent.getSurname();
        this.kindOfTalent = talent.getKind().getKind();
        this.profilePicture = talent.getAvatar();
    }
}
