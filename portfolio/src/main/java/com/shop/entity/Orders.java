package com.shop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.shop.config.BaseEntity;
import com.shop.constant.OrderStatus;

import lombok.Getter;
import lombok.Setter;

//주의!! Order로 하면 에러남 (Order가 정렬 예약어 이므로..)
@Getter @Setter
@Entity
public class Orders extends BaseEntity{ // 등록한사람, 수정한사람만 있는 entity + 상속받은 등록일 수정일 entity 도 있음

   @Id @GeneratedValue
   @Column(name="orders_id")
   private Long id;

   @ManyToOne // 한 명의 회원은 여러 번 주문을 할 수 있다
   @JoinColumn(name="member_id")
   private Member member;           //회원 엔티티 

   private LocalDateTime orderDate;  //주문일

   @Enumerated(EnumType.STRING)
   private OrderStatus orderStatus;  //주문 상태

   
   //양방향 연관 매핑 주인 설정(OrderItem이 주인임)
   //영속성 전이(cascade) - Order가 삭제되면 OrderItem도 함께 삭제됨
   @OneToMany(mappedBy="orders", cascade=CascadeType.ALL)
   private List<OrderItem> orderItems = new ArrayList<>();

   //주문 상품 추가
   public void addOrderItem(OrderItem orderItem) {
      orderItems.add(orderItem); // orderItems 에 주문 상품 정보들 넣어줌
      orderItem.setOrders(this);  // orderItems 과 양방향 매핑이기 때문에 orderItem 에다가도 order 객체를 넣어줌 (orderItems 은 order 객체임)
   }

   //주문 추가
   public static Orders createOrder(Member member, List<OrderItem> orderItemList) {
      Orders order = new Orders();
      order.setMember(member); //상품을 주문한 회원 세팅

      for(OrderItem orderItem : orderItemList) {
         order.addOrderItem(orderItem);
         // 상품 페이지에서는 1개의 상품을 주문하지만, 장바구니에는 여러 상품을 주문할 수 있다.
         // 그래서 장바구니에 여러 상품을 담을 수 있게 리스트 형태로 파라미터 값을 받아야 한다.  파라미터는 아까 주문한 orderItem 임!
      }

      order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 ORDER 로 바꿈
      order.setOrderDate(LocalDateTime.now()); // 현재 시간을 주문 시간으로 바꿈
      return order;
   }
   
   //총 주문 금액 계산(누적)
   public int getTotalPrice() {
      int totalPrice = 0;
      for(OrderItem orderItem : orderItems) {
         totalPrice += orderItem.getTotalPrice();
      }
      return totalPrice;
   }
   
   //주문 상태를 취소 상태로 바꿈
   public void cancelOrder() {
      this.orderStatus = OrderStatus.CANCEL;
      //같은 제품을 여러번 취소
      for(OrderItem orderItem : orderItems) {
         orderItem.cancel();
      }
   }
   
}