package com.shop.config;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
	
	// 등록한사람, 수정한사람만 있는 entity + 상속받은 등록일 수정일 entity 도 있음
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;  //등록자
	
	@LastModifiedBy
	private String modifiedBy;  //수정자
}
