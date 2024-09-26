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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "userrole",
uniqueConstraints = {
        @UniqueConstraint(name = "USER_ROLE_UK", columnNames = { "userid", "roleid" }) })
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userroleid")
	public int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid", nullable = false)
    private Role appRole;

	public UserRole() {
		super();
	}

	public UserRole(User user, Role appRole) {
		super();
		this.user = user;
		this.appRole = appRole;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getAppRole() {
		return appRole;
	}

	public void setAppRole(Role appRole) {
		this.appRole = appRole;
	}
	
	
	
}
