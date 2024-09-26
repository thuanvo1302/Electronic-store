package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.*;
import com.cnjava.ElectronicsStore.Repository.*;
import com.cnjava.ElectronicsStore.Service.*;

import java.util.*;
@Component
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    public long countNumOfUsers() {
        return userRepository.countNumOfUsers();
    }
    
    public void saveUser(User b) {
        userRepository.save(b);
    }
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
    
    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public String getLastUserOTPByEmail(String email) {
        return userRepository.getLastUserOTPByEmail(email);
    }
    public String getLastOtpRequestedTimeByEmail(String email) {
        return userRepository.getLastOtpRequestedTimeByEmail(email);
    }
    public List<User> getUsersToLimit(int page, int number){
        return userRepository.getUsersToLimit(page, number);
    }
    
    public void updateUserPasswordByEmail(String email, String password) {
        userRepository.updateUserPasswordByEmail(email, password);
    }
    public void updateUserOtpByEmail(String email, String otp, String date) {
        userRepository.updateUserOtpByEmail(email, otp, date);
    }
    
    public void updateUserInformationByEmail(String email, String name, String address, String phoneNumber) {
        userRepository.updateUserInformationByEmail(email, name, address, phoneNumber);
    }
}
