package com.cnjava.ElectronicsStore.Config;

import java.io.IOException;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnjava.ElectronicsStore.Service.UserDetailsServiceImpl;

import org.springframework.security.web.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.config.annotation.web.configuration.*;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	Encoder encoder = new Encoder();
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
	
    @Autowired
    public void globallyConfigure(AuthenticationManagerBuilder authentication) throws Exception {
    	PasswordEncoder _encoder = encoder.getPasswordEncoder();
    	DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsServiceImpl> configurer = authentication.userDetailsService(userDetailsService);
    	
    	configurer.passwordEncoder(_encoder);
    }
    
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (WebSecurity websecurity) -> {
        		websecurity
        		.ignoring()
        		.requestMatchers(
	        		"/js/**",
	        		"/lib/**",
	        		"/css/**",
	        		"/img/**",
	        		"/favicon.ico"
				);
        	}
        ;
    }
    
    public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.getWriter().print("Please Log In");
        }
    }

    public class MyAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2) throws IOException, ServletException {
            response.getWriter().print("Wrong Url Or You're Not Authorized");
        }
    }
 
    
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity.csrf((CsrfConfigurer<HttpSecurity> csrf) -> {
	        					csrf.disable();        		
        					}
    	);
    	
        // Pages for Guests
    	httpSecurity.authorizeHttpRequests(
        		(authorizeHttpRequests) -> {
	        			authorizeHttpRequests
						.requestMatchers(
					        		"/",
					        		"/login",
					        		"/img/*",
					        		"/loginfail",
					        		"/product/*",
					        		"/uploads/*",
					        		"/category/*",
					        		"/admin/register",
					        		"/logoutSuccessful",
					        		"/admin/register/save",
					        		
					        		"",
	    							"/pay",
	    							"/home",
	    							"/error",
									"/tragop",
									"/tintuc",
									"/lienhe",
									"/brand/*",
									"/addUser",
									"/message",
									"/sendOTP",
									"/cuahang",
									"/savemes",
									"/tintuc/*",
									"/register",
									"/products",
									"/warranty",
									"/security",
									"/delivery",
									"/searching",
									"/newPassword",
									"/filterBrand",
									"/thucudoimoi",
									"/filterCategory",
									"/forgetPassword",
									"/updatePassword"
						)
						.permitAll();
        		}
		);
    	
         
        
    	// Pages for any kinds of user (administrator included)
    	httpSecurity.authorizeHttpRequests(
    			(authorizeHttpRequests) -> {
	    				authorizeHttpRequests
	    				.requestMatchers(
	    						"/cart",
	    						"/order",
	    						"/cart/*",
	    						"/payment",
	    						"/getCode",
	    						"/checkout",
	    						"/buynow/*",
	    						"/userinfo",
	    						"/editorder",
	    						"/addcart/*",
	    						"/updateUser",
	    						"/orderdetail",
	    						"/searchorder",
	    						"/deletecart/*",
	    						"/deletecomment",
	    						"/updatequantity",
	    						"/submit-comment",
	    						"/payment/saveorder"
						)
	    				.hasAnyRole("ADMIN","USER");
				}
		);

        
    	
    	// Pages for Administrators Only
    	httpSecurity.authorizeHttpRequests(
    			(authorizeHttpRequests) -> {
    				authorizeHttpRequests
    				.requestMatchers(
    						"/admin",
    						"/addrole",
    						"/addBrand",
    						"/deletemes",
    						"/addProduct",
    						"/deleterole",
    						"/searchemail",
    						"/deleteValue",
    						"/renameBrand",
    						"/deleteBrand",
    						"/deleteBrand/*",
    						"/addCategory",
    						"/admin/brands",
    						"/admin/orders",
    						"/deleteProduct",
    						"/deleteProduct/*",
    						"/filteraccount",
    						"/renameCategory",
    						"/deleteCategory",
    						"/deleteCategory/*",
    						"/admin/products",
    						"/admin/messages",
    						"/editProduct/**",
    						"/addPerformance",
    						"/admin/mesdetail",
    						"/admin/product/**",
    						"/admin/categories",
    						"/admin/statistics",
    						"/admin/listaccount",
    						"/admin/accountdetail",
    						"/updateStatusProduct",
    						"/admin/orders/update-status"
					)
    				.hasRole("ADMIN");
    			}
		);
    	
    	
    	// define a customizer for access denied exception
    	Customizer<ExceptionHandlingConfigurer<HttpSecurity>> customizerForAccessDenied = 
    			(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfigurer) -> {
    				exceptionHandlingConfigurer.accessDeniedHandler(new MyAccessDeniedHandler());
				};
		httpSecurity.exceptionHandling(customizerForAccessDenied);
		
		
		// define a customizer for entry point authentication exception
		Customizer<ExceptionHandlingConfigurer<HttpSecurity>> customizerForEntryPointAuthentication =
				(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfigurer) -> {
    				exceptionHandlingConfigurer.authenticationEntryPoint(new MyAuthenticationEntryPoint());
    			};
		httpSecurity.exceptionHandling(customizerForEntryPointAuthentication);
        

		// define a customizer for log in form
		Customizer<FormLoginConfigurer<HttpSecurity>> customizerForFormLogin =
				(FormLoginConfigurer<HttpSecurity> formLoginConfigurer) -> {
					formLoginConfigurer
    				.usernameParameter("email")
    				.passwordParameter("password")
    				.loginPage("/login")
    				.failureUrl("/loginfail")
    				.defaultSuccessUrl("/");
			};
    	httpSecurity.formLogin(customizerForFormLogin);
        
    	
    	// define a customizer for log out form
    	Customizer<LogoutConfigurer<HttpSecurity>> customizerForFormLogout =
    			(LogoutConfigurer<HttpSecurity> formLogoutConfigurer) -> {
					formLogoutConfigurer.logoutUrl("/logout");
    			};
    	httpSecurity.logout(customizerForFormLogout);
        
    	
    	// define a customizer for log out form successfully
    	Customizer<LogoutConfigurer<HttpSecurity>> customizerForFormLogoutSuccess =
    			(LogoutConfigurer<HttpSecurity> formLogoutConfigurer) -> {
					formLogoutConfigurer.logoutSuccessUrl("/logoutSuccessful");
    			};
    	httpSecurity.logout(customizerForFormLogoutSuccess);
        
    	
    	
        HttpSessionRequestCache httpSessionRequestCache = new HttpSessionRequestCache();
        httpSessionRequestCache.setMatchingRequestParameterName(null);
        Customizer<RequestCacheConfigurer<HttpSecurity>> customizerForRequestCache =
        		(RequestCacheConfigurer<HttpSecurity> requestCacheConfigurer) -> {
        			requestCacheConfigurer.requestCache(httpSessionRequestCache);
        		};
        httpSecurity.requestCache(customizerForRequestCache);
        
        return httpSecurity.build();
    } 
}



























//httpSecurity.authorizeHttpRequests(
//		(authorizeHttpRequests) -> {
//				authorizeHttpRequests
//				.requestMatchers(
//						"",
//						"/pay",
//						"/home",
//						"/error",
//						"/tragop",
//						"/tintuc",
//						"/lienhe",
//						"/brand/*",
//						"/addUser",
//						"/message",
//						"/sendOTP",
//						"/cuahang",
//						"/savemes",
//						"/tintuc/*",
//						"/register",
//						"/products",
//						"/warranty",
//						"/security",
//						"/delivery",
//						"/searching",
//						"/newPassword",
//						"/filterBrand",
//						"/thucudoimoi",
//						"/filterCategory",
//						"/forgetPassword",
//						"/updatePassword"
//				)
//				.permitAll();
//		}
//);