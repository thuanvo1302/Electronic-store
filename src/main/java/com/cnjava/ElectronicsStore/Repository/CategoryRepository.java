package com.cnjava.ElectronicsStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import com.cnjava.ElectronicsStore.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query(   "SELECT count(c) "
			+ "from Category c")
	long countCategory();
	

}
