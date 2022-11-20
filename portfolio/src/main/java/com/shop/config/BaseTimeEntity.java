package com.shop.config;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

//등록일, 수정일만 있는 entity
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass  //부모 클래스를 상속받는 자식 클래스의 매핑 정보만 제공함
@Getter @Setter
public abstract class BaseTimeEntity {
	
	@CreatedDate // 엔티티 생성 시 시간 자동 저장
	@Column(updatable = false)
	private LocalDateTime regTime;
	
	@LastModifiedDate // 엔티티 수정 시 시간 자동 저장
	private LocalDateTime updateTime;
}
