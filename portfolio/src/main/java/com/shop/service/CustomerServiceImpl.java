package com.shop.service;

import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.shop.dto.CustomerDto;
import com.shop.dto.PageRequestDto;
import com.shop.dto.PageResultDto;
import com.shop.entity.Customer;
import com.shop.entity.QCustomer;
import com.shop.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor //생성자 주입(final 키워드 사용)
@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService{

   private final CustomerRepository repository;

	@Override
	public Long register(CustomerDto dto) {
		log.info("DTO");
	    log.info(dto);
	    
	    Customer entity = dtoToEntity(dto);
	    log.info(entity);
	    
	    repository.save(entity);
	    
	    return entity.getGno();
	}
	
	

		//조회수
		@Override
		public void updateCount(Long gno) {
			repository.updateCount(gno);
			
		}

		//게시글 삭제
		@Override
		public void remove(Long bno) {
			repository.deleteById(bno);
		}

		//게시글 수정
		@Override
		public void modify(CustomerDto dto) {
			//수정할 게시글 가져오기
			Customer customer = repository.findById(dto.getGno()).get();
			
			//게시글 수정
			customer.changeTitle(dto.getTitle());
			customer.changeContent(dto.getContent());
			
			//수정 저장
			repository.save(customer);
		}
	
		//글 목록보기
		@Override
		public PageResultDto<CustomerDto, Customer> getList(PageRequestDto requestDto) {
			//페이지 처리
			Pageable pageable = requestDto.getPageable(Sort.by("gno").descending());
			//검색처리
			BooleanBuilder booleanBuilder = getSearch(requestDto);
				
			Page<Customer> result = repository.findAll(booleanBuilder, pageable);
			Function<Customer, CustomerDto> fn = (entity -> entityToDto(entity));
			return new PageResultDto<>(result, fn);
		}

		//게시글 상세보기
			@Override
			public CustomerDto read(Long gno) {
				Optional<Customer> result  = repository.findById(gno);
				
				//찾아온 객체가 있으면 entity to dto를 호출 아니면 null 반환(삼항 연산자)
				return result.isPresent() ? entityToDto(result.get()) : null;
			}
			
			//검색 처리
			private BooleanBuilder getSearch(PageRequestDto requestDto) {
				String type = requestDto.getType();
				String keyword = requestDto.getKeyword();
				
				BooleanBuilder booleanBuilder = new BooleanBuilder();
				
				QCustomer qCustomer = QCustomer.customer;
				
				BooleanExpression expression = qCustomer.gno.gt(0L); // gno > 0 
				booleanBuilder.and(expression);
				
				//검색 조건이 없는 경우 null 처리
				if(type == null || type.trim().length() == 0 ) {
					return booleanBuilder;
				}
				
				
				//검색조건 작성
				BooleanBuilder conditionBuilder = new BooleanBuilder();
				if(type.contains("t")) {
					conditionBuilder.or(qCustomer.title.contains(keyword));
				}
				if(type.contains("c")) {
					conditionBuilder.or(qCustomer.content.contains(keyword));
				}
				if(type.contains("w")) {
					conditionBuilder.or(qCustomer.writer.contains(keyword));
				}
				//모든 조건 종합
				booleanBuilder.and(conditionBuilder);
				
				return booleanBuilder;
			}	
	   
	   
	}