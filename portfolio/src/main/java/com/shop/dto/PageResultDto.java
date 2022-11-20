package com.shop.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import lombok.Data;

@Data
public class PageResultDto<DTO, EN>{
   
	private List<DTO> dtoList; //dto 리스트
	
	private int totalPage;	// 총 페이지수
	private int page;	// 현재 페이지	
	
	private int size;		// 페이지당 글 개수
	private int start, end;	//시작페이지, 끝 페이지 번호
	private boolean prev, next;	//이전, 다음
	private List<Integer> pageList;	// 페이지 번호 목록 
	
   
   //생성자
   public PageResultDto(Page<EN> result, Function<EN, DTO> fn){
      dtoList = result.stream().map(fn).collect(Collectors.toList());
      totalPage = result.getTotalPages();
      makePageList(result.getPageable());
   }
   
   //페이지 목록 만들기 메서드
   public void makePageList(Pageable pageable) {
	   this.page = pageable.getPageNumber() + 1 ;
	   this.size = pageable.getPageSize();
	   
	   //real end 실제페이지 구현
	   int realEnd = (int)(Math.ceil(page/10.0)) * 10;
	   start = realEnd - 9;
	   end = totalPage > realEnd ? realEnd : totalPage;
	   
	   prev = start > 1;
	   next = totalPage > realEnd;
	   
	   pageList = IntStream.rangeClosed(start, end).boxed()
			   .collect(Collectors.toList());
   }
   
   
}