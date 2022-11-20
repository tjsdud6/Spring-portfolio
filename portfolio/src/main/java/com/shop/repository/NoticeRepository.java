package com.shop.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Notice;

public interface NoticeRepository  extends JpaRepository<Notice, Long>,
	QuerydslPredicateExecutor<Notice>{
	
	//조회수
	@Transactional
	@Modifying
	@Query("UPDATE Notice n SET n.cnt = n.cnt + 1 WHERE n.gno = :gno")
	void updateCount(@Param("gno") Long gno);
	
	//게시글 수정
	

}