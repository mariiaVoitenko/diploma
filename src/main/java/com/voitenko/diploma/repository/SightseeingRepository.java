package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Sightseeing;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sightseeing entity.
 */
public interface SightseeingRepository extends JpaRepository<Sightseeing,Long> {

}
