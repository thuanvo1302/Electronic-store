package com.cnjava.ElectronicsStore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Brand;
import com.cnjava.ElectronicsStore.Repository.BrandRepository;

@Service
public interface BrandService {
	void save(Brand b);
	
	List<Brand> getAllBrand();
	
	Brand getBrandById(int id);
	
	void deleteById(int id);
}
