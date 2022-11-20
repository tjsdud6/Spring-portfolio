package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.Member;
import com.shop.entity.OrderItem;
import com.shop.entity.Orders;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {
   
   private final ItemRepository itemRepo;
   private final MemberRepository memberRepo;
   private final OrderRepository orderRepo;
   private final ItemImgRepository itemImgRepo;
   
   //주문하기
   public Long order(OrderDto orderDto, String name) {
      //주문할 상품 조회
      Item item = itemRepo.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
      
      //로그인한 회원 가져오기
      //Member member = memberRepo.findByEmail(email);
      Member member = memberRepo.findByName(name);
      
      //주문할 상품 리스트를 이용
      List<OrderItem> orderItemList = new ArrayList<>();
      OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
      orderItemList.add(orderItem);
      
      //주문 엔티티 생성
      Orders order = Orders.createOrder(member, orderItemList);
      orderRepo.save(order);
      
      return order.getId();
   }
   
   //주문내역
   public Page<OrderHistDto> getOrderList(String name, Pageable pageable){
      //주문 조회
      List<Orders> orders = orderRepo.findOrders(name, pageable);
      //List<Orders> orders = orderRepo.findOrders(email, pageable);
      
      //주문 총 개수
      //Long totalCount = orderRepo.countOrder(email);
      Long totalCount = orderRepo.countOrder(name);
      
      //주문 리스트를 순회하면서 주문 내역 페이지에 전달할 dto 생성
      List<OrderHistDto> orderHistDtos = new ArrayList<>();
      for(Orders order : orders) {
         OrderHistDto orderHisDto = new OrderHistDto(order);
         List<OrderItem> orderItems = order.getOrderItems();
         for(OrderItem orderItem : orderItems) {
            //대표 이미지 조회
            ItemImg itemImg = 
                  itemImgRepo.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
            
            OrderItemDto orderItemDto = 
                  new OrderItemDto(orderItem, itemImg.getImgUrl());
            orderHisDto.addOrderItemDto(orderItemDto); //객체 1개 생성
         } //안쪽 for() 끝 
         orderHistDtos.add(orderHisDto);
      }//바깥쪽 for() 끝
       return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
   }
   //주문 취소 전 로그인한 사용자와 주문 데이터 생상한 사용자가 같은지 검사
   @Transactional(readOnly = true)
   public boolean validateOrder(Long orderId, String name) {
	   Member curMember = memberRepo.findByName(name); //현재 로그인 한 회원
      //Member curMember = memberRepo.findByEmail(email); //현재 로그인 한 회원
      Orders order = orderRepo.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
      
      Member savedMember = order.getMember();
      if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
         return false;
      }
      return true;
   }
   
   
   //주문 취소
   public void cancelOrder(Long orderId) {
      Orders order = orderRepo.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
      
      order.cancelOrder();
   }
   
   //장바구니 상품 주문하기
   public Long orders(List<OrderDto> orderDtoList, String name) {
	   Member member = memberRepo.findByName(name); 
	   //Member member = memberRepo.findByEmail(email);
	   List<OrderItem> orderItemList = new ArrayList<>();
	   
	   // 주문할 상품 리스트
	   for(OrderDto orderDto : orderDtoList) {
		   Item item = itemRepo.findById(orderDto.getItemId())
				   .orElseThrow(EntityNotFoundException::new);
		   OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
		   orderItemList.add(orderItem);
	   }
	   
	   //주문 객체 생성
	   Orders order = Orders.createOrder(member, orderItemList); // 회원이랑 주문할 상품 리스트들을 주문에 담음
	   orderRepo.save(order); //주문하기 저장
	   return order.getId();
   }
   
}