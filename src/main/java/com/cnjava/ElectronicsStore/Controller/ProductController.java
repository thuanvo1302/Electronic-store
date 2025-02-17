package com.cnjava.ElectronicsStore.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnjava.ElectronicsStore.Model.User;
import com.cnjava.ElectronicsStore.Model.Comment;
import com.cnjava.ElectronicsStore.Model.Product;
import com.cnjava.ElectronicsStore.Model.Specification;
import com.cnjava.ElectronicsStore.Repository.CommentRepository;
import com.cnjava.ElectronicsStore.Repository.UserRepository;
import com.cnjava.ElectronicsStore.Repository.UserRoleRepository;
import com.cnjava.ElectronicsStore.Service.BrandService;
import com.cnjava.ElectronicsStore.Service.CategoryService;
import com.cnjava.ElectronicsStore.Service.ProductService;
import com.cnjava.ElectronicsStore.Service.SpecificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private SpecificationService SpecificationService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@GetMapping(value = {"/", "/home"})
	public String index(Model model,Principal principal,HttpSession session) {

		if(principal != null) {
			String email = principal.getName();

			System.out.println(email);

			User user = userRepository.getUserByEmail(email);

			session.setAttribute("email", user.getEmail());
			session.setAttribute("username", user.getName());
		}

		Page<Product> samsungProduct = productService.getTop5ProductsByBrand(1);
		Page<Product> appleProduct = productService.getTop5ProductsByBrand(2);
		Page<Product> newPhone = productService.getTop5ProductsByCategory(1);
		Page<Product> newLaptop = productService.getTop5ProductsByCategory(2);

		model.addAttribute("samsung", samsungProduct.getContent());
		model.addAttribute("apple", appleProduct.getContent());
		model.addAttribute("newphone", newPhone.getContent());
		model.addAttribute("newlaptop", newLaptop.getContent());

		return "index.html";
	}

	@GetMapping("/products")
	public String getProductsPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "none") String sortingOptions, Model model) {
		Page<Product> productPage = productService.getProductsPageSorted(page, sortingOptions);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("sortingOptions", sortingOptions);
		model.addAttribute("webtitle", "Tất cả sản phẩm");

		return "products";
	}

	@GetMapping("/brand/{brandid}")
	public String getProductsPageByBrand(
			@PathVariable("brandid") int brandId,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "none") String sortingOptions,
			Model model,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {
		Page<Product> productPage = productService.getProductsPageSortedByBrand(brandId, page,type, sortingOptions);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("sortingOptions", sortingOptions);
		model.addAttribute("webtitle", brandService.getBrandById(brandId).getBrandName());
		model.addAttribute("list", categoryService.getAllCategory());
		model.addAttribute("id", brandId);
		model.addAttribute("page","brand");
		model.addAttribute("type",type);
		model.addAttribute("action","/filterCategory");


		return "products";
	}

	@GetMapping("/category/{categoryid}")
	public String getProductsPageByCategory(@PathVariable("categoryid") int categoryId, @RequestParam("page") int page, @RequestParam(defaultValue = "none") String sortingOptions, Model model,@RequestParam("type") int type) {
		Page<Product> productPage = productService.getProductsPageSortedByCategory(categoryId, page, type, sortingOptions);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("sortingOptions", sortingOptions);
		model.addAttribute("webtitle", categoryService.getCategoryById(categoryId).getCategoryName());
		model.addAttribute("list", brandService.getAllBrand());
		model.addAttribute("id", categoryId);
		model.addAttribute("page","category");
		model.addAttribute("type",type);
		model.addAttribute("action","/filterBrand");

		return "products";
	}

	@GetMapping("/searching")
	public String search(@RequestParam("keyword") String keyword, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "none") String sortingOptions) {
		Page<Product> productPage = productService.getProductsPageSortedByKeyword(keyword, page, sortingOptions);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("sortingOptions", sortingOptions);
		model.addAttribute("webtitle", keyword);

		return "products";
	}

	@GetMapping("/product/{id}")
	public String productDetail(@PathVariable("id") int id, Model model, HttpSession session) {
		Product product = productService.getProductById(id);
		List<Specification> specifications = SpecificationService.getSpecificationsByProductID(id);
		List<Comment> comments = commentRepository.findAllCommentsByProductID(id);

		Integer totalrate = 0;

		for(Comment comment: comments) {
			totalrate += comment.getRate();
		}

		String email = (String)session.getAttribute("email");

		User user = userRepository.getUserByEmail(email);

		model.addAttribute("product", product);
		model.addAttribute("performances", specifications);
		model.addAttribute("comments", comments);
		model.addAttribute("email", session.getAttribute("email"));

		if(comments.size() != 0) {
			model.addAttribute("totalrate", Math.round(totalrate / comments.size()));
		} else {
			model.addAttribute("totalrate", 0);
		}


		List<String> imageLinks = Arrays.asList(product.getImageLink().split(";"));

		model.addAttribute("imageLinks", imageLinks);

		if(user!= null) {
			List<String> role = userRoleRepository.getRoleNames(user.getUserID());

			String namerole = "ROLE_USER";

			for(String s : role) {
				if(s.equals("ROLE_ADMIN")) {
					namerole = "ROLE_ADMIN";
				}
			}

			model.addAttribute("namerole", namerole);
		}



		return "product";
	}

	@PostMapping("/submit-comment")
	public String submitComment(@RequestParam("commentText") String commentText,
								@RequestParam("rating") int rating,
								@RequestParam("productId") int productId,
								HttpSession session) {

		User user = userRepository.getUserByEmail((String) session.getAttribute("email"));
		Product product = productService.getProductById(productId);

		// Create a new Comment object and save it
		Comment comment = new Comment();
		comment.setCommenttext(commentText);
		comment.setRate(rating);
		comment.setUser(user);
		comment.setProduct(product);
		comment.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		commentRepository.save(comment);

		return "redirect:/product/" + productId;
	}

	@PostMapping("/filterBrand")
	public String filterBrand(@RequestParam("id1") int id1,@RequestParam("id2") int id2 ) {
		return "redirect:/category/"+id1+"?page=0&type=" + id2;
	}

	@PostMapping("/filterCategory")
	public String filterCategory(@RequestParam("id1") int id1,@RequestParam("id2") int id2 ) {
		return "redirect:/brand/"+id1+"?page=0&type=" + id2;
	}
}
