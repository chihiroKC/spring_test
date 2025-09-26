package jp.co.sss.spring.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewForm {

	private String dummyUserName;
	private Integer rating;
	
	@NotBlank(message = "メールアドレスは必須項目です。")
	@Email(message = "正しいメールアドレス形式で入力してください。")
	private String email;
	
	@Size(max = 300, message = "コメントは300文字以内で入力してください。")
	private String comment;

	
	public String getDummyUserName() {
		return dummyUserName;
	}

	public void setDummyUserName(String dummyUserName) {
		this.dummyUserName = dummyUserName;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
