package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemImgDto {
	
	private Long id;
	
	private String imgName;  //이미지 파일명
	
	private String oriImgName;  //이미지 원본 파일명
	
	private String imgUrl;    //이미지 조회 경로
	
	private String repimgYn;  //대표 이미지 여부
	
	//modelMapper를 사용하여 엔티티 객체와 Dto 객체간의 데이터를 복사하여
	//복사한 객체를 반환함
	private static ModelMapper modelMapper = new ModelMapper();
	
	// ModelMapper: 서로 다른 클래스의 값을! 필드의 이름과 자료형이 같을 때! getter, setter 를 통해 값을 복사해서 객체를 반환함
	public static ItemImgDto of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
		// entity 를 파라미터로 받아서 dto 로 반환함
	}
}
