package com.shop.dto;

import com.shop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

//상품 데이터 조회 시 상품 조건을 갖고 있음
@Getter @Setter
public class ItemSearchDto {
	
	// 현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회함
	private String searchDateType;  //등록일 기준 조건
	
	private ItemSellStatus searchSellStatus;  //판매 상태 기준 조건
	
	private String searchBy;   //검색 유형
	/*
    itemNm: 상품명
    createBy: 상품 등록자 아이디
     */
	
	
	private String searchQuery = "";  //조회할 검색어
	/*
    itemNm: 상품명 기준 검색
    createBy: 상품 등록자 아이디 검색
     */
}