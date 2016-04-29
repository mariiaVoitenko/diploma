package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Sightseeing_content;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sightseeing_content entity.
 */
public interface Sightseeing_contentRepository extends JpaRepository<Sightseeing_content,Long> {

}
