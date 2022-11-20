package com.shop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shop.dto.NoticeDto;
import com.shop.dto.PageRequestDto;
import com.shop.dto.PageResultDto;
import com.shop.entity.Notice;

@SpringBootTest
public class NoticeServiceTest {

	@Autowired
	private NoticeService service;
	
	//새글 등록 테스트
	/* @Test
	public void testRegister() {
		
		NoticeDto noticeDto = NoticeDto.builder()
				.title("예제")
				.content("예제")
				.writer("글쓴이")
				.build();
		System.out.println(service.register(noticeDto));
	} */
	
	//게시글 목록 보기
	/*@Test
	public void testList() {
		//페이지 요청
		PageRequestDto pageRequestDto = PageRequestDto.builder()
				.page(1).size(10).build();
		
		//페이지 결과 
		PageResultDto<NoticeDto, Notice> resultDto = 
				service.getList(pageRequestDto);
		System.out.println("prev: " + resultDto.isPrev());  
		System.out.println("next: " + resultDto.isNext());
		System.out.println("total: " + resultDto.getTotalPage());
		
		//반복 처리
		for(NoticeDto NoticeDto : resultDto.getDtoList()) {
			System.out.println(NoticeDto);
		}
		
		System.out.println("========================================");
		resultDto.getPageList().forEach(i -> System.out.print(i + " "));
	}*/
	
	/* @Test
	public void testSearch() {
		PageRequestDto pageRequestDto = PageRequestDto.builder()
				.page(1)
				.size(10)
				.type("t")
				.keyword("Title")
				.build();
		
		//목록 보기
		PageResultDto<NoticeDto, Notice> resultDto = 
				service.getList(pageRequestDto);
		
		System.out.println("prev: " + resultDto.isPrev());
		System.out.println("next: " + resultDto.isNext());
		System.out.println("total: " + resultDto.getTotalPage());
		
		for(NoticeDto NoticeDto : resultDto.getDtoList()) {
			System.out.println(NoticeDto);
		}
		
		//페이지 출력
		resultDto.getPageList().forEach(i -> System.out.print(i + " "));
		
	} */
	
}
