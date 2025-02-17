package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnjava.ElectronicsStore.Model.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	@Query(   "SELECT c "
			+ "FROM Cart c "
			+ "WHERE c.userid.email= :email")
	List<Cart> getCartByEmail(@Param("email") String email);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(   "DELETE "
			+ "FROM Cart c "
			+ "WHERE c.userid.email = :mail")
	void deleteCart(@Param("mail") String mail);
	
	
	@Query(   "SELECT c "
			+ "FROM Cart c "
			+ "WHERE c.userid.email = :email and c.productid.ProductID = :id and c.color = :color")
	Cart getCart(@Param("email") String email, @Param("id") int id, @Param("color") String color);
}
