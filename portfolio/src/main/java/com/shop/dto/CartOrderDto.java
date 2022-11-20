package com.shop.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartOrderDto {

	// 주문할 상품 데이터 전달용
	
	private Long cartItemId;	//품목 아이디
	
	private List<CartOrderDto> cartOrderDtoList; //품목 리스트
}
