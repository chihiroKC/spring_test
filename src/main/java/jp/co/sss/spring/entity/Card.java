package jp.co.sss.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cards_gen")
	@SequenceGenerator(name = "seq_cards_gen", sequenceName = "seq_cards", allocationSize = 1)

	@Column(name = "card_number")
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
    
    
}
