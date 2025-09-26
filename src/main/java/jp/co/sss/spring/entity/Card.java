package jp.co.sss.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cards")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cardId;

	@Column(name = "card_number", length = 15, nullable = false)
	@Size(max = 15, message = "カード番号は15桁以内で入力してください。")
	@Pattern(regexp = "[0-9]+", message = "カード番号は半角数字で入力してください。")
    private String cardNumber;
	
	@Column(name = "card_brand")
    private String cardBrand;
	
	@Column(name = "expiration")
    private String expiration;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Login login;

	public String getCardNumberMasked() {
		if (cardNumber != null && cardNumber.length() > 4) {
			return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
		}
		return "****";
	}	

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
}
