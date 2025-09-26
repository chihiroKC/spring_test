package jp.co.sss.spring.entity;



import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_gen")
	@SequenceGenerator(name = "seq_order_gen", sequenceName = "seq_order", allocationSize = 1)
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "total_amount")
	private Integer totalAmount;
	
	@Column
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Login login;
	
	
	private Integer quantity;
	private Integer price;
	
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "productId")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "sale_item_id", referencedColumnName = "saleItemId")
	private Sale sale;
	
	private static final BigDecimal TAX_RATE = new BigDecimal("0.1");

	@Transient
	private String displayName;
	
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
	}
	
	public Integer getTotalAmountWithTax() {
		if (totalAmount == null) {
			return 0;
		}
		
		BigDecimal amountWithTax = BigDecimal.valueOf(totalAmount)
				.multiply(BigDecimal.ONE.add(TAX_RATE));
		
		return amountWithTax.intValue();
	}


	public String getDisplayName() {
		if (sale != null) {
			return sale.getName();
		} else if (product != null) {
			return product.getName();
		}
		return "";
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayImgPath() {
		if (sale != null) {
			return sale.getSaleImgPath();
		} else if (product != null) {
			return product.getImgPath();
		}
		return null;
	}

}
