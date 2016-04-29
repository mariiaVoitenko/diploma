package com.voitenko.diploma.repository;

import com.voitenko.diploma.domain.Category;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
