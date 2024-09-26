package com.cnjava.ElectronicsStore.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.OrderDetail;


@Service
public interface OrderDetailService {

	
	void saveDetail(OrderDetail od);

	List<OrderDetail> getList(int id);
}
