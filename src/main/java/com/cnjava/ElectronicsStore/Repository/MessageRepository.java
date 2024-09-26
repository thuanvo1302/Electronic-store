package com.cnjava.ElectronicsStore.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cnjava.ElectronicsStore.Model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
		
	@Query(value = 
			  "Select * "
			+ "from message "
			+ "ORDER BY messageid DESC "
			+ "limit :page,:number",
			nativeQuery = true)
	List<Message> getMessage(@Param("page") int page, @Param("number") int n);
	
	@Query(   "SELECT m "
			+ "FROM Message m "
			+ "WHERE m.mesid = :id")
	Message getById(@Param("id") int id);
}
