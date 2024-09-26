package com.cnjava.ElectronicsStore.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cnjava.ElectronicsStore.Model.Brand;
import com.cnjava.ElectronicsStore.Model.Category;
import com.cnjava.ElectronicsStore.Service.BrandService;
import com.cnjava.ElectronicsStore.Service.CategoryService;

@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandService;
	
	//	Biến cục bộ
	@ModelAttribute
	public void addCommonAttributes(Model model) {
		List<Category> categories = categoryService.getAllCategory();
        List<Brand> brands = brandService.getAllBrand();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
	}
}
