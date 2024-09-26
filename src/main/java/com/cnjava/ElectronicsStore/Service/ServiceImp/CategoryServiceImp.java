package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Category;
import com.cnjava.ElectronicsStore.Repository.CategoryRepository;
import com.cnjava.ElectronicsStore.Service.CategoryService;

import java.util.List;

@Component
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    public long countCategory() {
        return categoryRepository.countCategory();
    }
}
