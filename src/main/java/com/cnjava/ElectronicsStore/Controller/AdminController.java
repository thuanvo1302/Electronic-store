package com.cnjava.ElectronicsStore.Controller;

import java.io.File;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cnjava.ElectronicsStore.Config.Encoder;
import com.cnjava.ElectronicsStore.Model.*;
import com.cnjava.ElectronicsStore.Repository.*;
import com.cnjava.ElectronicsStore.Service.*;
import com.cnjava.ElectronicsStore.Support.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AdminController {
	private final String uploadDirectory = "static/uploads/";
	
    @Autowired
    private UserService userService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
	private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private SpecificationService specificationService;
    
    
    @GetMapping("/admin")
    public String getAdminHomePage() {
    	return "admin/admin";
    }
    
    @GetMapping("/admin/register")
    public String getAdminRegistrationForm(Model model){
        model.addAttribute("admin", new User());
        return "admin/register";
    }
    
    @PostMapping("/admin/register/save")
    public String registerAsAdmin(@Validated @ModelAttribute("admin") User wantToRegisterAdmin, Model model){
       if(userService.getUserByEmail(wantToRegisterAdmin.getEmail()) == null) {
    	   wantToRegisterAdmin.setPassword(
    			   (new Encoder())
    			   .getPasswordEncoder()
    			   .encode(wantToRegisterAdmin.getPassword())
		   );
    	   wantToRegisterAdmin.setEnable(true);
    	   userService.saveUser(wantToRegisterAdmin);
    	   userRoleRepository.save(
    			   new UserRole(
    	    			   userService.getUserByEmail(wantToRegisterAdmin.getEmail()),
    	    			   roleRepository.findByName("ROLE_ADMIN")
    			   )
		   );
          
    	   return "redirect:/admin/register?success";
       }
       model.addAttribute("error","Already Existed Email! Please Try Again with a New Email");
       model.addAttribute("admin",wantToRegisterAdmin);
       return "/admin/register";
    }
    	
	@GetMapping(value = {"/admin/products"})
	public String manageProducts(Model model) {
		model.addAttribute(
				"brands",
				brandService.getAllBrand()
		);
		model.addAttribute(
				"products",
				productService.getAllProduct()
		);
		model.addAttribute(
				"categories",
				categoryService.getAllCategory()
		);
		return "admin/adminProduct";
	}
	
	@GetMapping(value = {"/admin/brands"})
	public String manageBrands(Model model) {
		model.addAttribute(
				"brands",
				brandService.getAllBrand()
		);
		return "admin/adminBrand";
	}
	
	@GetMapping(value = {"/admin/categories"})
	public String manageCategories(Model model) {
		model.addAttribute(
				"categories",
				categoryService.getAllCategory()
		);
		return "admin/adminCategory";
	}
	
	@PostMapping(value = {"/addProduct"})
	public String addProduct(
		    @RequestParam String productName,
		    @RequestParam int productPrice,
		    @RequestParam String productDescription,
		    @RequestParam("imageFiles") MultipartFile[] imageFiles,
		    @RequestParam int productBrand,
		    @RequestParam int productCategory,
		    @RequestParam("values") String values,
		    @RequestParam String productState,
		    @RequestParam String productColors,
		    @RequestParam String productSupples
			) throws Exception {
		    String imageUrls = "";
		    for(MultipartFile multipartFile : imageFiles)
			        if (!multipartFile.isEmpty()) {
			            String originalFilename = multipartFile.getOriginalFilename();
			            Path uploadPath = Paths.get(
			            		ResourceUtils
								.getFile("classpath:" + uploadDirectory)
								.getAbsolutePath() /*images folder*/
								+ File.separator + originalFilename
			            		/*fullPath*/
	            		);
	
			            if (!Files.exists(uploadPath))
			            	Files.write(uploadPath, multipartFile.getBytes());
			            else
			                FileCopyUtils.copy(multipartFile.getBytes(), uploadPath.toFile());
	
			            String imageUrl = "/uploads/" + originalFilename;
			            imageUrls = imageUrls + (imageUrl + ";");
			        }
		    Product product = new Product(
					productName,
					productPrice,
					productDescription,
		    		imageUrls,
		    		brandService.getBrandById(productBrand),
		    		categoryService.getCategoryById(productCategory),
					productColors,
					productState,
					productSupples,
		    		1
    		);
		    productService.save(product);
		    String[] specificationsAsList = values.split(";");
		    for (String specification : specificationsAsList) {
		        String[] specificationProperties = specification.split("_");
		        String specificationName = specificationProperties[0];
	            String specificationDetail = specificationProperties[1];
	            specificationService.save(
	            		new Specification(
	    	            		specificationName,
	    	            		specificationDetail,
	    	            		product
	            		)
        		);
		    }
		    return "redirect:/admin/products";
		}
	
	@PostMapping(value = {"/deleteProduct"})
	public String removeProductById( @RequestParam  int ProductID) {
		specificationService
		.getSpecificationsByProductID(ProductID)
	    .forEach(spec -> specificationService.deleteSpecificationById(spec.getID()));
		
		productService.deleteById(ProductID);
		return "redirect:/admin/products?success=delete_success";
	}
	
	@PostMapping(value = {"/addBrand"})
	public String addBrand(@RequestParam("brandName") String brandName) {		
		brandService.save(
			new Brand(brandName)
		);
		
		return "redirect:/admin/brands";
	}
	
	@PostMapping(value = {"/renameBrand"})
	public String updateBrandName(@RequestParam("brandId") int brandId, @RequestParam("newBrandName") String brandName) {
		Brand brand = brandService.getBrandById(brandId);
	    if (brand != null) {
	    	brand.setBrandName(brandName);
	    	brandService.save(brand);
	    }
	    return "redirect:/admin/brands";
	}
	
	@PostMapping(value = {"/deleteBrand"})
	public String removeBrandById( @RequestParam int brandID) {
		try {
			brandService.deleteById(brandID);
			return "redirect:/admin/brands?success=delete_success";
		} catch (Exception e) {
			// Log the exception or handle it as needed
			e.printStackTrace(); // You can replace this with proper logging

			// Return an error message
			return "redirect:/admin/brands?error=delete_error";
		}
	}
	
	@PostMapping(value = {"/addCategory"})
	public String addCategory(@RequestParam("categoryName") String categoryName) {		
		categoryService.save(
			new Category(categoryName)
		);
		return "redirect:/admin/categories";
	}
	
	@PostMapping(value = {"/renameCategory"})
	public String updateCategoryNameById(@RequestParam("categoryId") int categoryId, @RequestParam("newCategoryName") String name) {
	    Category category = categoryService.getCategoryById(categoryId);
	    if (category != null) {
	        category.setCategoryName(name);
	        categoryService.save(category);
	    }
	    return "redirect:/admin/categories";
	}

	@PostMapping(value = {"/deleteCategory"})
	public String deleteCategoryById(@RequestParam  int CategoryID) {
		try {
			categoryService.deleteById(CategoryID);
			return "redirect:/admin/categories?success=delete_success";

		} catch (Exception e) {
			// Log the exception or handle it as needed
			e.printStackTrace(); // You can replace this with proper logging

			// Return an error message
			return "redirect:/admin/categories?error=delete_error";
		}
	}
	
	@PostMapping(value = {"/filteraccount"})
	public String filterUserAccount(@RequestParam String type) {
		return "redirect:/admin/listaccount?page=1&type=" + type;
	}
	
	
	@GetMapping("/admin/listaccount")
	public String getAccountList(@RequestParam(value = "page", defaultValue = "1") int page,
					             @RequestParam(value = "type", defaultValue = "all") String type,
					             Model model) {
		int n = 10;

		if(type.equals("all")) {
			List<User> list = userService.getUsersToLimit(page*n-n, n);

			model.addAttribute("list", list);

			model.addAttribute("pre", page-1);
			model.addAttribute("size", list.size());
			model.addAttribute("counter", new Counter());
			model.addAttribute("title", "Tất cả tài khoản");
			return "admin/listaccount";
		}
		else {
			if(type.equals("user")) {
				List<UserRole> l = userRoleRepository.getLimitUser(page*n-n, n);

				List<User> list = new ArrayList<>();

				for(UserRole r : l) {
					list.add(r.getUser());
				}

				model.addAttribute("list", list);

				model.addAttribute("pre", page-1);
				model.addAttribute("size", list.size());
				model.addAttribute("counter", new Counter());
				model.addAttribute("title", "Tài khoản User");
				return "admin/listaccount";
			}


		}

		List<UserRole> l = userRoleRepository.getLimitAdmin(page*n-n, n);

		List<User> list = new ArrayList<>();

		for(UserRole r : l) {
			list.add(r.getUser());
		}

		model.addAttribute("list", list);

		model.addAttribute("pre", page-1);
		model.addAttribute("size", list.size());
		model.addAttribute("counter", new Counter());
		model.addAttribute("title", "Tài khoản Admin");


		return "admin/listaccount";
	}
	
	
	@GetMapping(value = {"/admin/orders"})
	public String manageOrders( @RequestParam(name = "status", required = false) Integer param_status,
						        @RequestParam(name = "keyword", required = false) String param_keyword,
						        Model model) {
		List<Order> orders = new ArrayList<>();
	    if (param_status != null)
	        orders = orderService.getOrdersByStatus(param_status);
	    else if (param_keyword != null)
	        orders = orderService.searchOrders(param_keyword);
	    else
	        orders = orderService.getAllOrders();
	    
	    model.addAttribute("orders", orders);
		return "admin/ordersManagement";
	}
	
	@PostMapping("/admin/orders/update-status")
	public String updateOrderStatus(@RequestParam("orderid") int orderId,
	                                @RequestParam("status") int status) {
	    Order order = orderService.getOrderById(orderId);
	    order.setStatus(status);
	    orderService.saveOrder(order);
	    return "redirect:/admin/orders";
	}
	
	@GetMapping("/admin/accountdetail")
	public String getDetailOfAccount(@RequestParam("id") int id, Model model) {
		model.addAttribute("counter", new Counter());
		model.addAttribute("user", userService.getUserById(id));
		model.addAttribute("list", userRoleRepository.getRole(id));
		
		return "admin/accountdetail";
	}
	
	@PostMapping("/deleterole")
	public String removeRole(@RequestParam("userid") int userId,@RequestParam("roleid") int roleId , RedirectAttributes redirectAttributes) {
		 userRoleRepository.deleteById(roleId);
		 redirectAttributes.addFlashAttribute("message", "Successfully Delete Role");
		 return "redirect:/admin/accountdetail?id=" + userId;
	}
	
	@PostMapping("/addrole")
	public String addRole(@RequestParam("userid") int userId,@RequestParam("type") int type , RedirectAttributes redirectAttributes) {
		String message = "Existing Role";
		if(userRoleRepository.check(userId, type) == null) {		
			userRoleRepository.save(
					new UserRole(
							userService.getUserById(userId),
							roleRepository.findById(type)
					)
			);
			message = "Successfully Authorize";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/admin/accountdetail?id=" + userId;
	}
	
	@PostMapping("/searchemail")
	public String getAccountDetailByEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		User user = userService.getUserByEmail(email);
		if(user != null) return "redirect:/admin/accountdetail?id=" + user.getUserID();

		redirectAttributes.addFlashAttribute("message", "Account not found");
		return "redirect:/admin/listaccount?page=1&type=all";
	}
	
	@GetMapping(value = {"/admin/messages"})
	public String getMessage(@RequestParam("page") int page, Model model) {
		int numOfMessagePerPage = 10;
		List<Message> messagesInPage = messageRepository.getMessage((page*numOfMessagePerPage) - numOfMessagePerPage, numOfMessagePerPage);
			model.addAttribute("pre", page - 1);
			model.addAttribute("list", messagesInPage);
			model.addAttribute("size", messagesInPage.size());
			model.addAttribute("counter", new Counter());
		return "admin/messagemanagement";
	}
	
	@GetMapping(value = {"/admin/mesdetail"})
	public String getDetailOfMessage(@RequestParam("id") int messageId, Model model) {
		Message message = messageRepository.getById(messageId);
		model.addAttribute("mes", message);
		return "admin/mesdetail";
	}
	
	@PostMapping("/deletemes")
	public String removeMessage(@RequestParam("mesid") int messageId, @RequestParam("page") int page, RedirectAttributes redirectAttributes) {
		messageRepository.deleteById(messageId);
		redirectAttributes.addFlashAttribute("message","Message Deleted Successfully");
		return "redirect:/admin/messages?page="+page;
	}
	
	@GetMapping("/admin/statistics")
    public String getBusinessStatistics(Model model) {	
		model.addAttribute("doanhthu", orderService.getTotal());
		model.addAttribute("soluongsp", productService.countProduct());
		model.addAttribute("danhmucsp", categoryService.countCategory());
		model.addAttribute("soluongdonhang", orderService.countOrder());
		model.addAttribute("soluonguser", userService.countNumOfUsers());
		
    	return "admin/statistics";
    }
	
	@PostMapping("/updateStatusProduct")
	public String updateProductStatus(@RequestParam("ProductID") int productId, @RequestParam("status") int param_status) {
		Product product = productService.getProductById(productId);
		product.setStatus(param_status);
		productService.save(product);
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/{id}")
	public String getDetailOfProductById(@PathVariable("id") int productId, Model model) {
        model.addAttribute("brands", brandService.getAllBrand());
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("performance", specificationService.getSpecificationsByProductID(productId));
        
		return "admin/editProduct";
	}
	
	@PostMapping("/editProduct/{id}")
	public String updateProductInfo(@PathVariable("id") int productId,
								    @ModelAttribute("product") Product updatedProduct,
								    @RequestParam("imageFiles") List<MultipartFile> imageFiles,
								    @RequestParam(value = "deleteImages", required = false) List<String> deleteImages) throws Exception {
	    Product productToUpdate = productService.getProductById(productId);
	    productToUpdate.copy(
	    		updatedProduct.getProductName(),
	    		updatedProduct.getDescription(),
	    		updatedProduct.getPrice(),
	    		updatedProduct.getState(),
	    		updatedProduct.getColors(),
	    		updatedProduct.getSupples(),
	    		brandService.getBrandById(updatedProduct.getBrand().getBrandID()),
	    		categoryService.getCategoryById(updatedProduct.getCategory().getCategoryID())
		);
	    // Add Images
        String afterAddImageLinks = (new ImageHelper())
        							.addImage(productToUpdate.getImageLink(), imageFiles);
        productToUpdate.setImageLink(afterAddImageLinks);
        
        // Delete Images
        String imageUrls = productToUpdate.getImageLink();
        
        String afterAddAndDeleteImages = (new ImageHelper())
										 .removeImages(deleteImages, imageUrls);
        productToUpdate.setImageLink(afterAddAndDeleteImages);
        
        productService.save(productToUpdate);
                
        return "redirect:/admin/product/{id}";
    }
	
	@PostMapping("/addPerformance")
	public String addPerformance(@RequestParam int productId, @RequestParam("name") String specificationName, @RequestParam("value") String specificationDetail) {
		specificationService.save(
				new Specification(
						specificationName,
						specificationDetail,
						productService.getProductById(productId)
				)
		);
		return "redirect:/admin/product/" + productId;
	}
	
	@PostMapping("/deleteValue")
	public String removeSpecification(@RequestParam("ValueID") int specificationId, @RequestParam("ProductID") int productId) {
		specificationService.deleteSpecificationById(specificationId);
		return "redirect:/admin/product/" + productId;
	}
}
