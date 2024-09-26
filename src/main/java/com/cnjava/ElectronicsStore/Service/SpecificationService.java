package com.cnjava.ElectronicsStore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Specification;
import com.cnjava.ElectronicsStore.Repository.SpecificationRepository;

@Service
public class SpecificationService {
	@Autowired
	private SpecificationRepository specificationRepository;

	public void save(Specification specification) {
		specificationRepository.save(specification);
	}

	public List<Specification> getAllSpecifications(){
		return specificationRepository.findAll();
	}

	public Specification getSpecificationById(int id) {
		return specificationRepository.findById(id).get();
	}

	public List<Specification> getSpecificationsByProductID(int id) {
		return specificationRepository.getSpecificationsByProductID(id);
	}

	public void deleteSpecificationById(int id) {
		specificationRepository.deleteById(id);
	}
}
