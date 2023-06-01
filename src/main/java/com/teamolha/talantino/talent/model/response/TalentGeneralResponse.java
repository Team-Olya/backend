package com.teamolha.talantino.talent.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.entity.Talent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TalentGeneralResponse {
    long id;
    String name;
    String surname;

    String kindOfTalent;
    String profilePicture;
    List<SkillDTO> skills;

    public TalentGeneralResponse(Talent talent){
        this.id = talent.getId();
        this.name = talent.getName();
        this.surname = talent.getSurname();
        this.kindOfTalent = talent.getKind().getKind();
        this.profilePicture = talent.getAvatar();
        this.skills = talent.getSkills().stream().map(SkillDTO::new).toList();
    }
}
