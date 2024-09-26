package com.cnjava.ElectronicsStore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Category;
import com.cnjava.ElectronicsStore.Repository.CategoryRepository;

@Service
public interface CategoryService {
	

	void save(Category category);
	
	List<Category> getAllCategory();
	
	Category getCategoryById(int id);
	
	void deleteById(int id);
	
	long countCategory();
}
