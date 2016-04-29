package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Content;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Content entity.
 */
public interface ContentRepository extends JpaRepository<Content,Long> {

}
