package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnjava.ElectronicsStore.Support.EmailHelper;
@Component

public class EmailServiceImp {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailMessageTo(String toEmail, String message) {
    	EmailHelper emailHelper = new EmailHelper();
    	
    	SimpleMailMessage simpleMailMessage = emailHelper.getSimpleMailMessage(
    			"nguyenminhkhang061003@gmail.com",/*From Email*/
    			toEmail,
    			"OTP Verification Code",/*Subject*/
    			("Mã OTP: " + message)
		);
        
        javaMailSender.send(simpleMailMessage);
    }
}
