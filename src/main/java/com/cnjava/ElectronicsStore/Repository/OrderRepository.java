package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cnjava.ElectronicsStore.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(   "SELECT or "
			+ "FROM Order or "
			+ "WHERE or.code= :code")
	Order getByCode(@Param("code") String code);
	
	@Query(value = 
			  "SELECT * "
			+ "FROM orders "
			+ "WHERE userid= :userid "
			+ "limit :offset,:number",
			nativeQuery = true)
	List<Order> getListOrder(@Param("userid") int userid, @Param("offset") int offset, @Param("number") int number);
	
	@Query(   "SELECT or "
			+ "FROM Order or "
			+ "WHERE or.userid.email= :mail and or.orderid= :id")
	Order getOrderIdEmail(@Param("mail") String mail, @Param("id") int id);
	
	@Query(   "SELECT o "
			+ "FROM Order o "
			+ "WHERE o.status= :status")
	List<Order> getOrdersByStatus(@Param("status") int status);
	
	@Query(   "SELECT o "
			+ "FROM Order o "
			+ "WHERE o.email LIKE %:keyword% OR o.phonenumber LIKE %:keyword%")
	List<Order> searchOrders(@Param("keyword") String keyword);
	
	@Query(   "SELECT SUM(o.total) "
			+ "FROM Order o "
			+ "where o.status = 1 or o.status = 2")
	long getTotal();
	
	@Query(   "Select count(o) "
			+ "FROM Order o")
	long countOrder();
}



