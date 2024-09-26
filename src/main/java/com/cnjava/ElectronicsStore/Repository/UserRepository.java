package com.cnjava.ElectronicsStore.Repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cnjava.ElectronicsStore.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(   "SELECT count(u) "
			+ "from User u")
	long countNumOfUsers();
		
	@Query(   "SELECT u.otp_requested_time "
			+ "FROM User u "
			+ "WHERE u.email = :param_email")
    String getLastOtpRequestedTimeByEmail(@Param("param_email") String email);
	
	@Query(   "SELECT u.otp "
			+ "FROM User u "
			+ "WHERE u.email = :param_email")
    String getLastUserOTPByEmail(@Param("param_email") String email);
	
	@Query(   "SELECT u "
			+ "FROM User u "
			+ "WHERE u.email = :param_email")
    User getUserByEmail(@Param("param_email")String email);
	
	@Query(value =
			"Select * "
			+ "from Users u "
			+ "limit ?1,?2",
			nativeQuery = true)
	List<User> getUsersToLimit(int page, int number);
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(   "Update User u "
			+ "set u.password = ?2 "
			+ "WHERE u.email = ?1")
	void updateUserPasswordByEmail(String email, String password);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(   "Update User u "
			+ "set u.otp = ?2, "
			+     "u.otp_requested_time = ?3 "
			+ "WHERE u.email = ?1")
	void updateUserOtpByEmail(String email, String otp, String date);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(   "Update User u "
			+ "set u.name = ?2, "
			+     "u.address = ?3, "
			+     "u.phoneNumber = ?4  "
			+ "WHERE u.email = ?1")
	void updateUserInformationByEmail(String email, String name, String address, String phoneNumber);
}
