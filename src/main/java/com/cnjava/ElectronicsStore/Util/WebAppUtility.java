package com.cnjava.ElectronicsStore.Util;

import com.cnjava.ElectronicsStore.Support.Supporter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;



public class WebAppUtility {
	public static String toString(User user) {
		Collection<GrantedAuthority> authorities = user.getAuthorities();
        StringBuilder sb = (new StringBuilder()).append("UserName:").append(user.getUsername());

        if ((new Supporter<GrantedAuthority>()).isNullOrEmptyCollection(authorities)) {
            sb.append(" (");

            Optional<GrantedAuthority> firstGrantedAuthority = authorities.stream().findFirst();
            sb.append(firstGrantedAuthority.get().getAuthority());
            authorities.stream().skip(1).forEach(authority -> {
                    sb.append(", ").append(authority.getAuthority());
            });
            
            sb.append(")");
        }
        
        return sb.toString();
    }
}
