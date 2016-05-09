package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Code;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Code entity.
 */
public interface CodeRepository extends JpaRepository<Code,Long> {

}
