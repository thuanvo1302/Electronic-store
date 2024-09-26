package com.cnjava.ElectronicsStore.Service;


import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Product;

@Service
public interface ProductService {

	
	void save(Product b);
	
	List<Product> getAllProduct();

	Product getProductById(int id);
	
	void deleteById(int id);
	

	
	Page<Product> getTop5ProductsByBrand(int brandId);
    Page<Product> getTop5ProductsByCategory(int categoryId);
    

    Page<Product> getProductsPageSorted(int page, String sortingOptions);
    
    Page<Product> getProductsPageSortedByBrand(int brandid, int page,int type, String sortingOptions);
	
	Page<Product> getProductsPageSortedByCategory(int categoryid, int page,int type ,String sortingOptions);
	Page<Product> getProductsPageSortedByKeyword(String keyword, int page, String sortingOptions);
	
	long countProduct();
}
