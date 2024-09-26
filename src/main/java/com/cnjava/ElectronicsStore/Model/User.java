package com.cnjava.ElectronicsStore.Model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
	//Table Columns Variables
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	public Integer id;
	public int getUserID() {
		return this.id;
	}
	public void setUserID(int id) {
		this.id = id;
	}
	
	
	@Column(name = "email", length = 255, unique = true)
	private String email;
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Column(name = "name", length = 255)
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Column(name = "password", length = 255)
	private String password;
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Column(name = "address", length = 255)
	private String address;
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	@Column(name = "phonenumber", length = 255)
	private String phoneNumber;
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	@Column(name ="otp_code")
	private String otp;
	public String getOtp() {
		return this.otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
	@Column(name="enable")
	private boolean isEnable;
	public boolean isEnable() {
		return this.isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	
	
	@Column(name ="otp_requested_time")
	private String otp_requested_time;
	public String getOtp_requested_time() {
		return this.otp_requested_time;
	}
	public void setOtp_requested_time(String otp_requested_time) {
		this.otp_requested_time = otp_requested_time;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Comment> comments;
	public List<Comment> getComments() {
		return this.comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
	//Constructors
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id, String email, String name, String password, String address, String phoneNumber, String otp, boolean isEnable, String otp_requested_time, List<Comment> comments) {
		super();
		this.id = id;
		this.otp = otp;
		this.name = name;
		this.email = email;
		this.address = address;
		this.isEnable = isEnable;
		this.comments = comments;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.otp_requested_time = otp_requested_time;
	}




	
	
	
}
