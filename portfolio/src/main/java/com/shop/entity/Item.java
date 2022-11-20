package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.shop.config.BaseEntity;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Entity
public class Item extends BaseEntity{
   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "item_id")
   private Long id;      //자동 순번
   
   @Column(nullable = false, length = 50)
   private String itemNm; //상품 이름
   
   @Column(nullable = false)
   private int price;   //상품 가격
   
   @Column(nullable = false)
   private int stockNumber; //재고 수량
   
   @Column(nullable = false)
   private String itemDetail; //상세 설명
   
   @Enumerated(EnumType.STRING)
   private ItemSellStatus itemSellStatus;
   
   //상품 수정 메서드
   public void updateItem(ItemFormDto itemFormDto) {
      this.itemNm = itemFormDto.getItemNm();
      this.price = itemFormDto.getPrice();
      this.stockNumber = itemFormDto.getStockNumber();
      this.itemDetail = itemFormDto.getItemDetail();
      this.itemSellStatus = itemFormDto.getItemSellStatus();   
   }
   /* 
   엔티티 클래스에 비즈니스 로직을 추가하면 객체지향적으로 코딩을 할 수 있고, 코드를 재활용 할 수도 있다.
   데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻) 
   엔티티 클래스에 비즈니스 로직(데이터 처리) 을 추가하면
   1. 객체지향적으로 코딩을 할 수 있고,
   2. 코드를 재활용 할 수도 있다.
   3. 데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻) 
    */
   
   //재고 감소 메소드(주문 시)
   public void removeStock(int stockNumber) {
      int restStock = this.stockNumber - stockNumber;
      if(restStock < 0) {
         throw new OutOfStockException("상품의 재고가 부족합니다."
               + "(현재 재고 수량: )" + this.stockNumber + ")"); //사용자 정의형 예외처리
      }
      //주문 후 남은 재고 수량을 상품의 현재 재고값에 저장
      this.stockNumber = restStock;
   }
   
   //재고 증가 메소드 (주문 취소시)
   public void addStock(int stockNumber) {
	   this.stockNumber += stockNumber;
   }
   

}