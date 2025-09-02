package jp.co.sss.spring.form;

import jakarta.validation.constraints.NotNull;

public class OrderForm {

	@NotNull
	private Integer orderId;
	@NotNull
	private Integer userId;
	@NotNull
	private Integer totalAmount;
	@NotNull
	private String status;
	
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmopunt(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
