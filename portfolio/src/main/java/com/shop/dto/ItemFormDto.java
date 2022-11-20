package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {
	// 여기에는 상품이랑, 상품 이미지가 다 있음
	
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력 값입니다.")
	private String itemNm;   //상품명
	
	@NotNull(message = "가격은 필수 입력 값입니다.")
	private Integer price;
	
	@NotBlank(message = "상세 설명은 필수 입력 값입니다.")
	private String itemDetail;  //상세 설명
	
	@NotNull(message = "재고는 필수 입력 값입니다.")
	private Integer stockNumber;  //재고 수량
	
	private ItemSellStatus itemSellStatus; //판매 상태
	
	 // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
	
	// 상품의 이미지 아이디를 저장하는 리스트
    // 상품 등록 전에는 이미지가 없으니까 비어있음(이미지도 공백, 아이디도 공백!)
    // 그냥 수정할 때 이미지 아이디 저장해둘 용도
	private List<Long> itemImgIds = new ArrayList<>();
	
	
	// ModelMapper: 서로 다른 클래스의 값을! 필드의 이름과 자료형이 같을 때! getter, setter 를 통해 값을 복사해서 객체를 반환함
	private static ModelMapper modelMapper = new ModelMapper();
	
	// this_entity 를 dto 로 반환함
	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}
	
	 // entity 를 파라미터로 받아서 dto 로 반환함
	public static ItemFormDto of(Item item) {
		return modelMapper.map(item, ItemFormDto.class);
	}
	
}
