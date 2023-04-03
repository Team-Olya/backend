package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByTalentId(long id);

    Boolean existsLinkByUrl(String url);

    long deleteByTalent(Talent talent);
}
