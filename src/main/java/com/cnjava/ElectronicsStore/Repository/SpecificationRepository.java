package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cnjava.ElectronicsStore.Model.Specification;


@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Integer> {
	@Query(value = "SELECT u FROM Specification u WHERE u.Product.ProductID = ?1")
	List<Specification> getSpecificationsByProductID(int id);
}
