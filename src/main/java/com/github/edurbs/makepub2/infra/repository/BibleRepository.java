package com.github.edurbs.makepub2.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.edurbs.makepub2.infra.infra.entity.Bible;

@Repository
public interface BibleRepository extends JpaRepository<Bible, Long> {
    
} 
