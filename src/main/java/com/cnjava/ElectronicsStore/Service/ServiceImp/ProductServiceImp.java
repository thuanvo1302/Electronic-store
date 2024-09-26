package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Product;
import com.cnjava.ElectronicsStore.Repository.ProductRepository;
import com.cnjava.ElectronicsStore.Service.ProductService;

import java.util.List;

@Component
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void save(Product b) {
        productRepository.save(b);
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).get();
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }



    public Page<Product> getTop5ProductsByBrand(int brandId) {
        return productRepository.findTop5ByBrand(brandId, PageRequest.of(0, 6));
    }

    public Page<Product> getTop5ProductsByCategory(int categoryId) {
        return productRepository.findTop5ByCategory(categoryId, PageRequest.of(0, 6));
    }


    public Page<Product> getProductsPageSorted(int page, String sortingOptions) {
        long totalProducts = productRepository.count();
        int pageSize = (int) Math.min(20, Math.max(1, totalProducts));

        Pageable pageable;

        if (sortingOptions.equals("increasing")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("price")));
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("price")));
        }

        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsPageSortedByBrand(int brandid, int page,int type, String sortingOptions) {
        long totalProducts = 0;

        if(type == 0) {
            totalProducts =  productRepository.countByBrand(brandid);
        }
        else {
            totalProducts =  productRepository.countByBrandCategory(brandid,type);
        }



        int pageSize = (int) Math.min(20, Math.max(1, totalProducts));

        Pageable pageable;

        if (sortingOptions.equals("increasing")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("price")));
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("price")));
        }

        if(type == 0) {
            return productRepository.findByBrand(brandid, pageable);
        }

        return productRepository.findByBrandCategory(brandid, type,pageable);
    }

    public Page<Product> getProductsPageSortedByCategory(int categoryid, int page,int type ,String sortingOptions) {


        long totalProducts = 0;

        if(type == 0) {
            totalProducts = productRepository.countByCategory(categoryid);
        }
        else {
            totalProducts = productRepository.countByCategoryBrand(categoryid,type);
        }

        int pageSize = (int) Math.min(20, Math.max(1, totalProducts));

        Pageable pageable;

        if (sortingOptions.equals("increasing")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("price")));
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("price")));
        }

        if(type != 0) {
            return productRepository.findByCategoryBrand(categoryid,type ,pageable);
        }

        return productRepository.findByCategory(categoryid ,pageable);
    }

    public Page<Product> getProductsPageSortedByKeyword(String keyword, int page, String sortingOptions) {
        long totalProducts = productRepository.countProductByKeyword(keyword);
        int pageSize = (int) Math.min(20, Math.max(1, totalProducts));

        Pageable pageable;

        if (sortingOptions.equals("increasing")) {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("price")));
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("price")));
        }

        return productRepository.getProductByKeyword(keyword, pageable);
    }

    public long countProduct() {
        return productRepository.countProduct();
    }
}
