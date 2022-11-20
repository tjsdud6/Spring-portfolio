package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto {
	
	// 장바구니 조회 페이지에 전달
	// 장바구니에 들어있는 상품들을 조회하기 위해
	
	private Long cartItemId;  //장바구니 상품 아이디
	
	private String itemNm; //상품명
	
	private int price;  //가격
	
	private int count;  //수량
	
	private String imgUrl; //상품 이미지

	//생성자
	public CartDetailDto(Long cartItemId, String itemNm, int price, 
			int count, String imgUrl) {
		this.cartItemId = cartItemId;
		this.itemNm = itemNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
	}
}