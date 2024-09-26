package com.cnjava.ElectronicsStore.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cart")
public class Cart {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cartid")
	public int cartID;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", nullable = false)
	private User userid;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productid", nullable = false)
	private Product productid;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "color")
	private String color;

	public Cart() {
		super();
	}

	public Cart(int cartID, User userid, Product productid, int quantity, int price, String color) {
		super();
		this.cartID = cartID;
		this.userid = userid;
		this.productid = productid;
		this.quantity = quantity;
		this.price = price;
		this.color = color;
	}
	
	public Cart(User userId, Product productId, int quantity) {
		super();
		this.userid = userId;
		this.productid = productId;
		this.quantity = quantity;
		this.price = productId.getPrice();
	}

	public Cart(User userId, String color, int quantity, Product productId, int price) {
		this.userid = userId;
		this.color = color;
		this.quantity = quantity;
		this.productid = productId;
		this.price = price;
	}
	
	public int getCartID() {
		return cartID;
	}

	public void setCartID(int cartID) {
		this.cartID = cartID;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public Product getProductid() {
		return productid;
	}

	public void setProductid(Product productid) {
		this.productid = productid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
}
