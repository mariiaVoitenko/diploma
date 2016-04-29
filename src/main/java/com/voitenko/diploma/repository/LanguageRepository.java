package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Language;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Language entity.
 */
public interface LanguageRepository extends JpaRepository<Language,Long> {

}
