package com.teamolha.talantino.talent.model.response;

import com.teamolha.talantino.talent.model.entity.Talent;
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
