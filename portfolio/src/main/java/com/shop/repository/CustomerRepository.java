package com.shop.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Customer;

public interface CustomerRepository  extends JpaRepository<Customer, Long>,
	QuerydslPredicateExecutor<Customer>{
	
	
	//조회수
	@Transactional
	@Modifying
	@Query("UPDATE Customer c SET c.cnt = c.cnt + 1 WHERE c.gno = :gno")
	void updateCount(@Param("gno") Long gno);

}
