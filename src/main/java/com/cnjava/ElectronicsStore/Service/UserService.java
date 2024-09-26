package com.cnjava.ElectronicsStore.Service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.*;

@Service
public interface UserService {
	long countNumOfUsers();
	
	void saveUser(User b);
	void deleteUserById(int id);
	
	User getUserById(int id);
	User getUserByEmail(String email);
	
	String getLastUserOTPByEmail(String email);
	String getLastOtpRequestedTimeByEmail(String email);
	
	List<User> getUsersToLimit(int page, int number);
	
	void updateUserPasswordByEmail(String email, String password);
	void updateUserOtpByEmail(String email, String otp, String date);
	
	void updateUserInformationByEmail(String email, String name, String address, String phoneNumber);	
}
