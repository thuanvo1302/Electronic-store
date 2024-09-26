package com.cnjava.ElectronicsStore.Service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Order;

@Service
public interface OrderService {


	void saveOrder(Order o);
	
	Order getOrderByCode(String code);
	List<Order> getListOrder(int userid, int offset, int number);
	Order getOrderById(int id);
	
	Order getOrderIdMail(String email, int id);

	List<Order> getAllOrders();
	
	List<Order> getOrdersByStatus(int status);
	
	List<Order> searchOrders(String keyword);
	
	long getTotal();
	
	long countOrder();
}
