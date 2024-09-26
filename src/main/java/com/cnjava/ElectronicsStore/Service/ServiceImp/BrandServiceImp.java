package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Brand;
import com.cnjava.ElectronicsStore.Repository.BrandRepository;
import com.cnjava.ElectronicsStore.Service.BrandService;

import java.util.List;

@Component
public class BrandServiceImp  implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public void save(Brand b) {
        brandRepository.save(b);
    }

    public List<Brand> getAllBrand(){
        return brandRepository.findAll();
    }

    public Brand getBrandById(int id) {
        return brandRepository.findById(id).get();
    }

    public void deleteById(int id) {
        brandRepository.deleteById(id);
    }
}
