package com.cnjava.ElectronicsStore.Config;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class Encoder {
	 @Bean
	  public PasswordEncoder getPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	  }
}
