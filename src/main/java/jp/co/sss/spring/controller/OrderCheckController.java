package jp.co.sss.spring.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Order;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Sale;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.OrderRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.SaleRepository;
import jp.co.sss.spring.security.UserDetailsImpl;
import jp.co.sss.spring.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderCheckController {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Transactional
	@RequestMapping("/check")
	public String showOrderCheck(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		
		List<Order> orders = orderRepository.findByUserIdAndStatus(login.getUserId(), "NEW");
		
		model.addAttribute("login", login);
		model.addAttribute("orders", orders);
		
		return "order/order_check";
	}
	
	
	@GetMapping("/order_detail")
	@Transactional(readOnly = true)
	public String editOrder(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		List<Order> orders = orderRepository.findByUserIdAndStatus(login.getUserId(), "NEW");
		
		
		model.addAttribute("login", login);
		model.addAttribute("orders", orders);
		
		return "order/order_detail";
	}
	
	@PostMapping("/confirm")
	public String confirmOrder(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		List<Order> orders = orderRepository.findByUserIdAndStatus(login.getUserId(), "NEW");
		
		
		for (Order order : orders) {
			if (order.getSale() != null) {
				order.setDisplayName(order.getSale().getName());
			} else if (order.getProduct() != null) {
				order.setDisplayName(order.getProduct().getName());
			}
		}
		
		model.addAttribute("login", login);
		model.addAttribute("orders", orders);
		
		return "order/order_check";
	}
	
	@PostMapping("/complete")
	@Transactional
	public String orderComplete(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();

		
	List<Order> orders = orderRepository.findByUserIdAndStatus(login.getUserId(), "NEW");
	
	for (Order order : orders) {
		order.setStatus("COMPLETED");
		orderRepository.save(order);
	}
	
	model.addAttribute("orders", orders);
	model.addAttribute("login", login);
	
	return "order/order_complete";
	}
	
	@PostMapping("/cart_add")
	@Transactional
	public String showCartAdd(Model model, 
			                  @RequestParam("productId") Integer productId, 
			                  @RequestParam(value = "saleItemId", required = false) Integer saleItemId,
			                  @RequestParam("quantity") Integer quantity) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		Product product = productRepository.findById(productId).orElseThrow();
		
		Order order = new Order();
		order.setLogin(login);
		order.setProduct(product);
		order.setQuantity(quantity);


		
		if (saleItemId != null) {
			Sale sale = saleRepository.findById(saleItemId).orElseThrow();
			order.setSale(sale);
			
			BigDecimal discounted = sale.getDiscountedPrice();
			BigDecimal total = discounted.multiply(BigDecimal.valueOf(quantity));
			
			order.setTotalAmount(total.intValue());
		} else {
			order.setTotalAmount(product.getPrice() * quantity);
		}
		
		order.setStatus("NEW");
		orderRepository.save(order);
		
		model.addAttribute("orders", List.of(order));
		
		return "order/cart_add";
	}
	
	@GetMapping("/cart_detail")
	public String showCartDetail(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		List<Order> orders = orderRepository.findByUserIdAndStatus(login.getUserId(), "NEW");
	    if (orders.isEmpty()) {
			return "redirect:/top";
		}
		
		model.addAttribute("orders", orders);
		model.addAttribute("login", login);
		
		return "order/cart_detail";
		
	}
}
