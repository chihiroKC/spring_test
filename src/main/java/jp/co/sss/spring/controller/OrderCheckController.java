package jp.co.sss.spring.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.http.HttpSession;

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

import jp.co.sss.spring.entity.Card;
import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Order;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Sale;
import jp.co.sss.spring.repository.CardRepository;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.OrderRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.SaleRepository;
import jp.co.sss.spring.security.UserDetailsImpl;
import jp.co.sss.spring.service.OrderService;
import jp.co.sss.spring.service.SaleService;

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
	
	@Autowired CardRepository cardRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SaleService saleService;
	
	@Transactional
	@RequestMapping("/check")
	public String showOrderCheck(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		
		List<Order> orders = orderRepository.findByLoginUserIdAndStatus(login.getUserId(), "NEW");
		
		 int cartTotalPrice = orders.stream()
		         .mapToInt(Order::getTotalAmount)
		         .sum();
 
		model.addAttribute("cartTotalPrice", cartTotalPrice);
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
		
		List<Order> orders = orderRepository.findByLoginUserIdAndStatus(login.getUserId(), "NEW");
		
		int cartTotal = orders.stream()
				       .mapToInt(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0)
				       .sum();
		

		model.addAttribute("cartTotal", cartTotal);
		
		if(orders.isEmpty()) {
			return "redirect:/top";
		}
		
		model.addAttribute("login", login);
		model.addAttribute("orders", orders);
		
		return "order/order_detail";
	}
	
	@PostMapping("/confirm")
	public String confirmOrder(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		List<Order> orders = orderRepository.findByLoginUserIdAndStatus(login.getUserId(), "NEW");
		
		
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

		
	List<Order> orders = orderRepository.findByLoginUserIdAndStatus(login.getUserId(), "NEW");
	
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
			                  @RequestParam("quantity") Integer quantity,
			                  HttpSession session) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		Product product = productRepository.findById(productId).orElseThrow();
		
		Order order = new Order();
		order.setLogin(login);
		order.setProduct(product);
		order.setQuantity(quantity);


		int totalAmount = 0;
		
		if (saleItemId != null) {
			Sale sale = saleRepository.findById(saleItemId).orElseThrow();
			order.setSale(sale);
			
			BigDecimal discountedPrice = saleService.calculateDiscountedPrice(sale);
			totalAmount = discountedPrice.multiply(BigDecimal.valueOf(quantity)).intValue();
		
		} else {
			totalAmount = product.getPrice()*quantity;
		}
		
		order.setTotalAmount(totalAmount);
		
		
		order.setStatus("NEW");
		orderRepository.save(order);
		
	    session.setAttribute("lastAddedOrderId", order.getOrderId());
		
		return "redirect:/order/cart_add_complete";
	}
	
	@GetMapping("/cart_add_complete")
	public String showCartAddComplete(Model model, HttpSession session) {
		Integer lastAddedOrderId = (Integer) session.getAttribute("lastAddedOrderId");
		
		if(lastAddedOrderId == null) {
			return "redirect:/top";
		}
		
		Order lastAddedOrder = orderRepository.findById(lastAddedOrderId).orElseThrow();
		
		model.addAttribute("orders", List.of(lastAddedOrder));
		
		model.addAttribute("successMessage", "カートに商品を追加しました");
		
		session.removeAttribute("lastAddedOrderId");
		
		return "order/cart_add";
	}
	
	@GetMapping("/cart_detail")
	public String showCartDetail(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		List<Order> orders = orderRepository.findByLoginUserIdAndStatus(login.getUserId(), "NEW");

		
		int totalQuantity = orders.stream()
				.mapToInt(order -> order.getQuantity() != null ? order.getQuantity() : 0)
				.sum();
		
		int subtotal = orders.stream()
				.mapToInt(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0)
				.sum();
		
		int cartTotal = orders.stream()
				.mapToInt(order -> order.getTotalAmountWithTax() != null ? order.getTotalAmountWithTax() : 0)
				.sum();
		
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("subtotal", subtotal);
		model.addAttribute("cartTotal", cartTotal);
		
	    if (orders.isEmpty()) {
			return "redirect:/top";
		}
		
		model.addAttribute("orders", orders);
		model.addAttribute("login", login);
		
		return "order/cart_detail";
		
	}
	
	@PostMapping("/save_details")
	@Transactional
	public String saveOrderDetails(
			@RequestParam String addressSelection,
			@RequestParam(required = false) String newAddress,
			@RequestParam(required = false) String newApartment,
			@RequestParam(required = false) String cardSelection,
			@RequestParam(required = false) Integer existingCardId,
			@RequestParam(required = false) String newCardNumber,
			@RequestParam(required = false) String newExpiration,
			HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = loginRepository.findById(userDetails.getLogin().getUserId()).orElseThrow();
		
		if("new".equals(addressSelection) && newAddress != null && !newAddress.isEmpty()) {
			login.setAddress(newAddress);
			login.setApartment(newApartment);
			loginRepository.save(login);
		}
		
		if("new".equals(cardSelection) && newCardNumber != null && !newCardNumber.isEmpty()) {
			Card newCard = new Card();
			newCard.setCardNumber(newCardNumber);
			newCard.setExpiration(newExpiration);
			newCard.setLogin(login);
			cardRepository.save(newCard);
		}
		
		return "redirect:/order/check";
	}
	
}
