package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.OrderDetail;
import com.cnjava.ElectronicsStore.Repository.OrderDetailRepository;
import com.cnjava.ElectronicsStore.Service.OrderDetailService;

import java.util.List;
@Component

public class OrderDetailServiceImp implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderdetailRes;


    public void saveDetail(OrderDetail od) {
        orderdetailRes.save(od);
    }



    public List<OrderDetail> getList(int id){
        return orderdetailRes.getList(id);
    }
}
