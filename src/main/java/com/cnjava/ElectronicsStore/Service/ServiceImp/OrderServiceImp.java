package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Order;
import com.cnjava.ElectronicsStore.Repository.OrderRepository;
import com.cnjava.ElectronicsStore.Service.OrderService;

import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;


    public void saveOrder(Order o) {
        orderRepository.save(o);
    }

    public Order getOrderByCode(String code) {
        return orderRepository.getByCode(code);
    }

    public List<Order> getListOrder(int userid, int offset, int number){
        return orderRepository.getListOrder(userid, offset, number);
    }

    public Order getOrderById(int id) {
        Optional<Order> getOrder = orderRepository.findById(id);
        return getOrder.get();
    }

    public Order getOrderIdMail(String email, int id) {
        return orderRepository.getOrderIdEmail(email, id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(int status) {
        return orderRepository.getOrdersByStatus(status);
    }

    public List<Order> searchOrders(String keyword) {
        return orderRepository.searchOrders(keyword);
    }

    public long getTotal() {
        return orderRepository.getTotal();
    }

    public long countOrder() {
        return orderRepository.countOrder();
    }
}
