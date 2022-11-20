package com.shop.service;

import com.shop.dto.NoticeDto;
import com.shop.dto.PageRequestDto;
import com.shop.dto.PageResultDto;
import com.shop.entity.Notice;

public interface NoticeService {

   //게시글 등록(추상 메소드)
   Long register(NoticeDto dto);
   
   //게시글 목록
   PageResultDto<NoticeDto, Notice> getList(PageRequestDto requestDto);
   
   //게시글 상세보기
   NoticeDto read(Long gno);
   
   //조회수
   void updateCount(Long gno);
   
   	//게시글 삭제
 	void remove(Long gno);
 	
 	//게시글 수정
 	void modify(NoticeDto dto);
   
   
   
   //자바 8버전부터 구체 메소드 사용 가능(default 키워드로 가능)
   default Notice dtoToEntity(NoticeDto dto) {
      Notice entity = Notice.builder()
                              .gno(dto.getGno())
                              .title(dto.getTitle())
                              .content(dto.getContent())
                              .writer(dto.getWriter())
                              .cnt(dto.getCnt())
                              .build();
      return entity;
   }
   
   default NoticeDto entityToDto(Notice entity) {
      NoticeDto dto = NoticeDto.builder()
               .gno(entity.getGno())
               .title(entity.getTitle())
               .content(entity.getContent())
               .writer(entity.getWriter())
               .cnt(entity.getCnt())
               .reg_time(entity.getRegTime())
               .update_time(entity.getUpdateTime())
               .build();
      
         return dto;
      }

   
   
   
   
}