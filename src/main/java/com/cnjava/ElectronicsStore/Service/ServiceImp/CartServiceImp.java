package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Cart;
import com.cnjava.ElectronicsStore.Repository.CartRepository;
import com.cnjava.ElectronicsStore.Service.CartService;

import java.util.List;
import java.util.Optional;

@Component
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;


    public List<Cart> getCartByEmail(String email){
        return cartRepository.getCartByEmail(email);
    }

    public Cart getById( int id) {
        Optional<Cart> tmp = cartRepository.findById(id);
        return tmp.get();
    }

    public void saveCart(Cart c) {
        cartRepository.save(c);
    }

    public void deleteCart(int cartID) {
        cartRepository.deleteById(cartID);
    }

//	public void deleteByUserEmail(String email) {
//		cartRepository.deleteCart(email);
//	}

    public Cart getCart(String email, int id, String color) {
        return cartRepository.getCart(email, id, color);
    }
}
