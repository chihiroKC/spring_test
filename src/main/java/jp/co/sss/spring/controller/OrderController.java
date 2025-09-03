package jp.co.sss.spring.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Order;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.OrderRepository;
import jp.co.sss.spring.security.UserDetailsImpl;
import jp.co.sss.spring.service.OrderService;

@Controller
public class OrderController {

	@Autowired
	private LoginRepository loginrepository;
	
	@Autowired
	private OrderRepository orderrepository;
	
	@Autowired
	private OrderService orderService;
	
	@Transactional
	@RequestMapping("/order") 
		public String showOrder(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		 Hibernate.initialize(login.getCards());
		
		 List<Order>orders = orderrepository.findByUserIdAndStatusOrderByOrderIdDesc(login.getUserId(), "NEW");

		model.addAttribute("orders", orders);
		model.addAttribute("login", login);
		
		return "order/order_check";
	}
	
	@PostMapping("/order/single_purchase")
	public String singlePurchase(
			@RequestParam("productId") Integer productId,
			@RequestParam(value = "saleItemId", required = false) Integer saleItemId,
			@RequestParam("quantity") int quantity
			) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Login login = userDetails.getLogin();
		
		if (saleItemId != null) {
			orderService.createSingleOrderWithSale(productId, saleItemId, quantity, login);
		} else {
			orderService.createSingleOrder(productId, quantity, login);
		}
		
		return "redirect:/order";
	}
	
	@PostMapping("/order/remove")
	public String removeOrder(@RequestParam("orderId") Integer orderId,
			@RequestParam("removeQuantity") int removeQuantity) {
		Order order = orderrepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("注文が見つかりません"));
		
		int oldQuantity = order.getQuantity();
		int newQuantity = oldQuantity - removeQuantity;
		
		if (newQuantity > 0) {
			
			BigDecimal oldTotal = BigDecimal.valueOf(order.getTotalAmount());
			BigDecimal unitPrice = oldTotal.divide(BigDecimal.valueOf(oldQuantity), RoundingMode.HALF_UP);
			
			BigDecimal newTotal = unitPrice.multiply(BigDecimal.valueOf(newQuantity));
			
			order.setQuantity(newQuantity);
			order.setTotalAmount(newTotal.intValue());
						
            orderrepository.save(order);
		}else {
			orderrepository.delete(order);
		}
		return "redirect:/order";
	}
}
