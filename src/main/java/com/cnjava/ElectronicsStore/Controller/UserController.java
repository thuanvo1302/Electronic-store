package com.cnjava.ElectronicsStore.Controller;

import java.util.*;
import java.time.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cnjava.ElectronicsStore.Model.*;
import com.cnjava.ElectronicsStore.Config.*;
import com.cnjava.ElectronicsStore.Service.*;
import com.cnjava.ElectronicsStore.Support.*;
import com.cnjava.ElectronicsStore.Repository.*;
import com.cnjava.ElectronicsStore.Service.ServiceImp.*;

import jakarta.servlet.http.*;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private EmailServiceImp mailService;
	@Autowired
	private CartService cartService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CommentRepository commentRepository;
	
	@GetMapping(value = {"/login","/sendOTP","/updatePassword","/updateUser"})
	public String getLoginFormPath() {
		return "login";
	}
	
	@GetMapping(value = {"/forgetPassword"})
	public String getForgetPasswordFormPath() {
		return "forgetpassword";
	}
	
	@GetMapping(value = {"/newPassword"})
	public String getNewPasswordFormPath() {
		return "newpassword";
	}
	
	@GetMapping(value = {"/message"})
	public String getMessageFormPath() {
		return "message";
	}
	
	@GetMapping("/payment")
	public String getPaymentPath() {
		return "payment";
	}
	
	@GetMapping(value = {"/register"})
	public String getRegisterFormPath(Model model) {
		model.addAttribute("User", new User());
		return "register";
	}


	
	@PostMapping(value = {"/addUser"})
	public String CreateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		String userEnteredEmail = user.getEmail();
			if(!(new EmailHelper(userService))
				.isExistingUserByEmail(userEnteredEmail)
			)
			{
				//Encode Password and Create User
				PasswordEncoder passwordEncoder = (new Encoder()).getPasswordEncoder();
				user.setPassword(passwordEncoder
								 .encode(
									user.getPassword()/*User's Entered Password*/
								)/*Encoded Password*/
				);
				user.setEnable(true);
					userService.saveUser(user);
				
				//Authorize
				UserRole userRole = new UserRole();
				userRole.setUser(userService.getUserByEmail(userEnteredEmail));
				userRole.setAppRole(roleRepository.findByName("ROLE_USER"));
					userRoleRepository.save(userRole);
				
				return "redirect:/login?success";
			}
			// Already Existed Account
			redirectAttributes.addFlashAttribute("error","There's an Existing Account with This Email\n"
												+ "Please Try Again with a Different Email");
			return "redirect:/register";
	}
	
	@PostMapping(value = {"/sendOTP"})
	public String sendOtpCode(@RequestParam String email, HttpSession httpSession, RedirectAttributes redirectAttributes) {		
		if((new EmailHelper(userService)).isExistingUserByEmail(email)) {
			String otpCode = (new RandomHelper()).randomOtp(6);
				mailService.sendMailMessageTo(email, otpCode);
				userService.updateUserOtpByEmail(
						email,
						otpCode,
						LocalDateTime
						.now()
						.format(DateTimeFormatter
								.ofPattern("yyyy-MM-dd HH:mm")
						)/*Formatted Datetime*/
				);
				httpSession.setAttribute("emailAttribute", email);
			return "redirect:/newPassword";
		}
		redirectAttributes.addFlashAttribute("error","The Account Registered with This Email Doesn't Exist\n"
													+ "Please Try Again with another Email");
		return "redirect:/forgetPassword";
	}
	
	
	@PostMapping(value = {"/updatePassword"})
	public String createNewUserPassword(@RequestParam String otp,@RequestParam String password, HttpSession httpSession, RedirectAttributes redirectAttributes) {
		String email = (String) httpSession.getAttribute("emailAttribute");		
		long minutesHavePast =  Duration
								.between(
									LocalDateTime
									.parse( userService.getLastOtpRequestedTimeByEmail(email),
											DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
									),
									LocalDateTime.now()
								)
								.toMinutes();
		if(!(userService.getLastUserOTPByEmail(email)).equals(otp)) {
			redirectAttributes.addFlashAttribute("error","OTP Code is Incorrect");
			return "redirect:/newPassword";
		}
		else if(minutesHavePast > 5){
			redirectAttributes.addFlashAttribute("error","OTP Code Has Expired");
			return "redirect:/newPassword";
		}
		// correct otp and still hasn't expired yet
		String encodedPassword = (new Encoder())
								.getPasswordEncoder()
								.encode(password);
			userService.updateUserPasswordByEmail(email, encodedPassword);
		redirectAttributes.addFlashAttribute("message","Bạn đã thay đổi mật khẩu thành công");
		httpSession.removeAttribute("emailAttribute");
		return "redirect:/message";
	}
	
	@GetMapping(value = {"/userinfo"})
	public String getUserInformation(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model) {
		String email;
		if((email = (String) httpSession.getAttribute("email")) != null) {
			User user = userService.getUserByEmail(email);
				model.addAttribute("email", user.getEmail());
				model.addAttribute("username", user.getName());
				model.addAttribute("address", user.getAddress());
				model.addAttribute("phoneNumber", user.getPhoneNumber());
				
				model.addAttribute("user", new User());
			return "userinfo";
		}
		redirectAttributes.addFlashAttribute("message", "Log In to View Your Profile");
		return "redirect:/message";
	}
	
	@PostMapping(value= {"/updateUser"})
	public String updateUserInformation(@RequestParam String name, @RequestParam String address, @RequestParam String phoneNumber,  HttpSession httpSession ) {	
		userService.updateUserInformationByEmail(
				(String) httpSession.getAttribute("email"),
				name,
				address,
				phoneNumber
		);
		httpSession.setAttribute("username", name);
		return "redirect:/userinfo";
	}
	
	@PostMapping(value = {"/signUp"})
	public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession httpSession) {
		User user = userService.getUserByEmail(email);
	    if (user != null && user.getPassword().equals(password)) {
	    	httpSession.setAttribute("email", email);
	    	httpSession.setAttribute("username", user.getName());
	        return "redirect:/home";
	    } else {
	        model.addAttribute("error", "Invalid Email or Password\n"
	        							+ "Please Try Again");
	        return "login";
	    }
	}
	
	@GetMapping(value={"/logoutSuccessful"})
	public String logout(Model model, Principal principal, HttpServletRequest httpServletRequest) {
		if(principal == null) return "redirect:/";
		
		HttpSession session;
		if((session = httpServletRequest.getSession(false)) != null) session.invalidate();
		
		return "redirect:/login";
	}
	
	
	@GetMapping("/order")
	public String getUserOrders(HttpServletRequest httpServletRequest, Model model, @RequestParam("page") int page) {
        HttpSession httpSession = httpServletRequest.getSession(false);
		if(httpSession == null) {return "redirect:/login";}
		
		int number = 10, offset = (page*number) - number;
		User user = userService
				 	 .getUserByEmail(
						 (String) httpSession.getAttribute("email")
					 );
		List<Order> orders = orderService.getListOrder(user.getUserID(), offset, number);
		if(orders.size() != 0) {
			model.addAttribute("pre", page-1);
			model.addAttribute("list", orders);
			model.addAttribute("counter", new Counter());
			return "order";
		}
		model.addAttribute("message", "There's no orders");
		return "message";
	}

	
	@GetMapping("/cart")  
	public String getCart(HttpServletRequest httpServletRequest, Model model) {
		HttpSession httpSession = httpServletRequest.getSession(false);
		if(httpSession == null) return "redirect:/login";
		
		List<Cart> carts =  cartService
							.getCartByEmail(
									(String) httpSession.getAttribute("email")
							);
		if(carts.size() != 0) {
			int totalPrice = carts.stream().mapToInt(Cart::getPrice).sum();
			model.addAttribute("total", totalPrice);
			model.addAttribute("list", carts);
			model.addAttribute("counter", new Counter());
			return "cart";
		}
		model.addAttribute("message", "Cart = 0");
		return "message";
	}

	
	@PostMapping(value = "/cart/{id}")
	public String addProductToCart(HttpServletRequest httpServletRequest, @PathVariable("id") int productId) {
		HttpSession httpSession = httpServletRequest.getSession(false);
		
		if(httpSession == null) return "redirect:/login";
	
		User user = userService.getUserByEmail(
				(String) httpSession.getAttribute("email")
		);
		cartService.saveCart(
				new Cart(
					user,
					productService.getProductById(productId),
					1
				)
		);
		return "redirect:/cart";
	}

	
	@PostMapping("/updatequantity")
	public String updateCartQuantity(@RequestParam("quantity") int quantity, @RequestParam("idcart") int cartId) {
		Cart cart = cartService.getById(cartId);
			cart.setQuantity(quantity);
			cart.setPrice(quantity * (cart.getProductid()).getPrice());
				cartService.saveCart(cart);
		return "redirect:/cart";
	}
	
	
	@PostMapping("/getCode")
	public String getCouponCode(@RequestParam("code") String code,  RedirectAttributes redirectAttributes) {
		Code gotCouponCode = codeService.getCodeByText(code);
		if(gotCouponCode != null) {
			if(gotCouponCode.getStatus() != 1) {
				redirectAttributes.addFlashAttribute("code", code);
				redirectAttributes.addFlashAttribute("discount", gotCouponCode.getPrice());
				return "redirect:/cart";
			}
			redirectAttributes.addFlashAttribute("error","Used Coupon Code, Can not use again");
			return "redirect:/cart";
		}
		redirectAttributes.addFlashAttribute("error","Invalid Coupon Code");
		return "redirect:/cart";
	}
	
	@GetMapping("/deletecart/{id}")
	public String clearCart(@PathVariable("id") int cartId) {
		cartService.deleteCart(cartId);
		return "redirect:/cart";
	}

	
	@PostMapping("/checkout")
	public String getPayment(@RequestParam("discount") String couponCode, Model model, HttpServletRequest httpServletRequest) {	
		List<Cart> carts = cartService.getCartByEmail(
				(String)(httpServletRequest
						.getSession(false)
						.getAttribute("email")
						)
		);
		int totalPrice = carts.stream().mapToInt(Cart::getPrice).sum();
		model.addAttribute("total", totalPrice);
		if(couponCode.equals("notdiscount")) {
			model.addAttribute("value", 0);
		} else {
			model.addAttribute("value", codeService
										.getCodeByText(couponCode)
										.getPrice()
							  );
			model.addAttribute("textcode",couponCode);
		}
		return "payment";
	}
	
	
	@GetMapping("/orderdetail")
	public String getOrderDetail(@RequestParam("id")int orderId, Model model,  HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession(false);
		
		if(httpSession != null) {
			Order order = orderService.getOrderIdMail(
					(String) httpSession.getAttribute("email"),
					orderId
			);
			
			Code code = codeService.getCodeByText(order.getDiscount());
			
			if(code != null)
				model.addAttribute("discount", code.getPrice());
			else
				model.addAttribute("discount", 0);

			model.addAttribute("counter", new Counter());
			model.addAttribute("or", order);
			model.addAttribute("code", code);
			model.addAttribute("list", orderDetailService.getList(orderId));
			
			return "orderdetail";
		 }
		 return "redirect:/login";
	}
	
	
	@PostMapping("/searchorder")
	public String findForOrder(@RequestParam("search") String key, Model model) {
		Order order = orderService.getOrderByCode(key);
		if(order != null) return "redirect:/orderdetail?id=" + order.getOrderid();
		model.addAttribute("message", "Can not find your order");
		return "message";
	}
	
	
	@PostMapping("/editorder")
	public String modifyOrder(@RequestParam("ordercode") String orderCode, @RequestParam("fullname")String fullName, @RequestParam("email") String email, @RequestParam("phone") String phoneNumber, @RequestParam("country-state") String area, @RequestParam("address") String address) {
		Order order = orderService.getOrderByCode(orderCode);
			order.setEmail(email);
			order.setProvince(area);
			order.setAddress(address);
			order.setFullname(fullName);
			order.setPhonenumber(phoneNumber);
		orderService.saveOrder(order);
		return "redirect:/orderdetail?id=" + order.getOrderid();
	}
	
	@GetMapping("/warranty")
	public String getWarrantyPath() {
		return "warranty";
	}
	

	@GetMapping("/security")
	public String getSecurityPath() {
		return "security";
	}
	
	@GetMapping("/delivery")
	public String getDeliveryPath() {
		return "delivery";
	}
	
	@GetMapping("/pay")
	public String getPay() {
		return "pay";
	}
	
	@GetMapping("/tragop")
	public String getTraGopPath() {
		return "tragop";
	}
	
	
	@GetMapping(value={"/loginfail"})
	public String getLoginFailedPath(Model model, Principal principal) {
		if(principal != null) return "redirect:/";
		model.addAttribute("error", "Wrong Username or Password");	
		return "login";
	}
	
	@PostMapping("/addcart/{id}")
	public String addCart(@PathVariable("id") int productId, @RequestParam("color") String color, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		String email = (String)( httpServletRequest
								 .getSession(false)
							 	 .getAttribute("email"));
		Cart cart = cartService.getCart(email, productId,color);
		String addedNotiString = " Added to Cart";
			if(cart == null) {
				Product product = productService.getProductById(productId);
				cart = new Cart(
						userService.getUserByEmail(email),
						color,
						1,
						product,
						product.getPrice()
				);
			} else {
				int currentQuantity = cart.getQuantity();
				cart.setQuantity(currentQuantity + 1);
				cart.setPrice(cart.getProductid().getPrice() * (currentQuantity + 1));
			}
		cartService.saveCart(cart);
		redirectAttributes.addFlashAttribute("message", cart.getProductid().getProductName() + addedNotiString);
		return "redirect:/product/"+productId;
	}
	
	
	@PostMapping("/buynow/{id}")
	public String buyNow(@PathVariable("id") int id, @RequestParam("color") String color, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		String email = (String)( httpServletRequest
								.getSession(false)
								.getAttribute("email"));
		Cart cart = cartService.getCart(email, id, color);
		if(cart == null) {
			Product product = productService.getProductById(id);
			cart = new Cart(
					userService.getUserByEmail(email),
					color,
					1,
					product,
					product.getPrice()
			);
		} else {
			int currentQuantity = cart.getQuantity();
			cart.setQuantity(currentQuantity + 1);
			cart.setPrice(cart.getProductid().getPrice() * (currentQuantity + 1));
			cartService.saveCart(cart);
		}
		cartService.saveCart(cart);
		return "redirect:/cart";
	}
	
	@GetMapping("/thucudoimoi")
	public String getRenewPath() {
		return "thucu";
	}
	
	@GetMapping("/lienhe")
	public String getContactPath() {
		return "lienhe";
	}
	
	@GetMapping("/cuahang")
	public String getStorePath() {
		return "cuahang";
	}
	
	@PostMapping("/savemes")
	public String saveMessage(@RequestParam("name") String name,
							  @RequestParam("email") String email,
							  @RequestParam("title") String title,
							  @RequestParam("message") String messageText,
							  RedirectAttributes redirectAttributes ) {
		
		Message message = new Message(
				name,
				email,
				title,
				messageText,
				(new SimpleDateFormat("dd-M-yyyy hh:mm:ss a")).format(new Date())
		);		
		messageRepository.save(message);
		
		redirectAttributes.addFlashAttribute("mes","Message Sent");
		
		return "redirect:/lienhe";
		
	}

	
	@GetMapping("/tintuc")
	public String getNews(@RequestParam("page") int page, Model model) {
		List<News> listNews = newsRepository.getList((page*5)-5, 5);
		if(listNews.size() != 0) {
			model.addAttribute("list", listNews);
			model.addAttribute("pre", page-1);
			return "tintuc";
		}
		model.addAttribute("message", "Nothing News");
		return "message";
		
	}
	
	
	@GetMapping("/tintuc/{title}")
	public String getContent(@PathVariable("title") String fileName, Model model) {
		News news = newsRepository.getByFileName(fileName);
			model.addAttribute("title", news.getTitle());
			model.addAttribute("name", news.getUserid().getName());
			model.addAttribute("date", news.getDate());
		return fileName;
	}
	
	@GetMapping("/error")
	public String getErrorPath(Model model) {
		model.addAttribute("message","Error! Something Bad Happend!");
		return "message";
	}
	
	@PostMapping("/deletecomment")
	public String deleteComment(@RequestParam("commentid") int commentId,
								@RequestParam("productid") int productId,
								HttpServletRequest httpServletRequest) {

		String email = (String) (httpServletRequest.getSession(false).getAttribute("email"));
		if(commentRepository.checkComment(commentId, email) == null) {
			List<String> roles = userRoleRepository.getRoleNames(
					userService.getUserByEmail(email).getUserID()
			);
			
			if(roles.contains("ROLE_ADMIN")) {
				commentRepository.deleteById(commentId);
				return "redirect:/product/" + productId;
			}
			return "redirect:/product/" + productId;
		} else {
			commentRepository.deleteById(commentId);
			return "redirect:/product/" + productId;
		}
	}
	
}
