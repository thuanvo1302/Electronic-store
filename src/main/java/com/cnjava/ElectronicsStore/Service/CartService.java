package com.cnjava.ElectronicsStore.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Cart;
import com.cnjava.ElectronicsStore.Repository.CartRepository;


@Service
public interface CartService {


	
	
	List<Cart> getCartByEmail(String email);
	Cart getById( int id) ;
	void saveCart(Cart c);
	void deleteCart(int cartID);

	
	Cart getCart(String email, int id, String color);
}
