package com.shop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.OrderItem;
import com.shop.entity.Orders;
import com.shop.service.OrderService;

@Transactional
@SpringBootTest
public class OrderTest {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	ItemRepository itemRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	/*
	public Item saveItem() {
		Item item = new Item();
		item.setItemNm("test 상품");
		item.setPrice(10000);
		item.setItemDetail("test 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		return itemRepo.save(item);
	}
	
	//회원 저장
	public Member saveMember() {
		Member member = new Member();
		member.setEmail("test1@test.com");
		return memberRepo.save(member);
	}
	
	@Test
	public void order() {
		Item item = saveItem();
		Member member = saveMember();
		
		OrderDto orderDto = new OrderDto();
		orderDto.setItemId(item.getId());
		orderDto.setCount(10);
		
		Long orderId = orderService.order(orderDto, member.getEmail());
		Orders order = orderRepo.findById(orderId)
				.orElseThrow(EntityNotFoundException::new);
		
		List<OrderItem> orderItem = order.getOrderItems();
		int totalPrice = orderDto.getCount() * item.getPrice();
		System.out.println(totalPrice);
		
		assertEquals(totalPrice, order.getTotalPrice());
	}
	*/
}
