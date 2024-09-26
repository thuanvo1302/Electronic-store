package com.cnjava.ElectronicsStore.Model;




import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "specifications")
public class Specification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "specificationid")
	public int id;

	@Column(name = "name")
	private String name;

	@Column(name = "detail")
	private String detail;

	@ManyToOne
	@JoinColumn(name = "productid")
	private Product Product;

	public Specification() {
		super();
	}
	public Specification(int id, String name, String detail, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.Product = product;
	}
	public Specification(String name, String detail, Product product) {
		super();
		this.name = name;
		this.detail = detail;
		this.Product = product;
	}
	public int getID() {
		return this.id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return this.detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Product getProduct() {
		return this.Product;
	}
	public void setProduct(Product product) {
		this.Product = product;
	}


}
