package com.teamolha.talantino.repository;

import com.teamolha.talantino.model.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByTalentId(long id);
}
