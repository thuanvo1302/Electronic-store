package com.cnjava.ElectronicsStore.Support;

import org.springframework.mail.SimpleMailMessage;

import com.cnjava.ElectronicsStore.Model.User;
import com.cnjava.ElectronicsStore.Service.*;

public class EmailHelper {
	public UserService userService;
	public EmailHelper(UserService userService) {
		this.userService = userService;
	}
	public EmailHelper() {
		super();
	}
	public SimpleMailMessage getSimpleMailMessage(String fromEmail, String toEmail, String subject, String message) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
		return simpleMailMessage;
	}
	
	public boolean isExistingUserByEmail(String email) {
		User temporaryUser = userService.getUserByEmail(email);
		return temporaryUser != null;
	}
}
