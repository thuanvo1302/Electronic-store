package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnjava.ElectronicsStore.Model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
//	@Transactional
//	@Modifying(clearAutomatically = true)
//	@Query(
//		  "UPDATE Brand b "
//		+ "SET b.BrandName = ?2 "
//		+ "WHERE b.BrandID = ?1 "
//	)
//	void deleteBrandById(int id);
}
