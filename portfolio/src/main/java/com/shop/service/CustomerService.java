package com.shop.service;

import com.shop.dto.CustomerDto;
import com.shop.dto.PageRequestDto;
import com.shop.dto.PageResultDto;
import com.shop.entity.Customer;
import com.shop.entity.Notice;

public interface CustomerService {

   //게시글 등록(추상 메소드)
   Long register(CustomerDto dto);
   
   //게시글 목록
   PageResultDto<CustomerDto, Customer> getList(PageRequestDto requestDto);
   
   //게시글 상세보기
   CustomerDto read(Long gno);
   
   //게시글 삭제
   void remove(Long bno);
	
	//게시글 수정
	void modify(CustomerDto dto);
   
   //조회수
   void updateCount(Long gno);
   
   
   
   //자바 8버전부터 구체 메소드 사용 가능(default 키워드로 가능)
   default Customer dtoToEntity(CustomerDto dto) {
      Customer entity = Customer.builder()
                              .gno(dto.getGno())
                              .title(dto.getTitle())
                              .content(dto.getContent())
                              .writer(dto.getWriter())
                              .cnt(dto.getCnt())
                              .build();
      return entity;
   }
   
   default CustomerDto entityToDto(Customer customer) {
	   CustomerDto dto = CustomerDto.builder()
               .gno(customer.getGno())
               .title(customer.getTitle())
               .content(customer.getContent())
               .writer(customer.getWriter())
               .cnt(customer.getCnt())
               .build();
         return dto;
      }

   
   
   
   
}