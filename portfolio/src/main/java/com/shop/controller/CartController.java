package com.shop.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.service.CartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CartController {
   
   private final CartService cartService;
   
   //장바구니 담기
   @PostMapping("/cart")
   public @ResponseBody ResponseEntity<?> order(
         @RequestBody @Valid CartItemDto cartItemDto,
         BindingResult bindingResult, Principal principal){
      
      //유효성 검증 cartItemDto 객체에 데이터 바인딩 시 에러있는지 검사
      if(bindingResult.hasErrors()) {
         StringBuilder sb = new StringBuilder();
         List<FieldError> fieldErrors = bindingResult.getFieldErrors();
         for(FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage());
         }
         return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
      }
      
      String name = principal.getName();
      Long cartItemId;
      
      try {
         cartItemId = cartService.addCart(cartItemDto, name);
      }catch(Exception e) {
         return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST); // 장바구니에 잘 안담겼으면 404
      }
      
      return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 장바구니에 상품이 잘 담기면 200
   }
   
   // 유저 이름을 받아와서, 카트 데이터들을 받아온다
   //장바구니 목록(페이지) 보기
   @GetMapping("/cart")
   public String cartList(Principal principal, Model model) {
      List<CartDetailDto> cartDetailList = 
            cartService.getCartList(principal.getName());
      model.addAttribute("cartItems", cartDetailList);
      return "cart/cartList";
   }
   
   //장바구니 상품 수량 업데이트
   @PatchMapping("/cartItem/{cartItemId}")
   public @ResponseBody ResponseEntity<?> updateCartItem(
	       @PathVariable("cartItemId") Long cartItemId, int count ,Principal principal){
	    if(count <= 0) {
	    	return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
	      }else if(!cartService.validateCartItem(cartItemId, principal.getName())) {
	    	  return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
	      }
	      cartService.updateCartItem(cartItemId, count);	//상품 수정 로직 호출
	      return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	   }
   
   //장바구니 상품 삭제
   @DeleteMapping("/cartItem/{cartItemId}")
   public @ResponseBody ResponseEntity<?> deleteCartItem(
	       @PathVariable("cartItemId") Long cartItemId,Principal principal){
	   
	    if(!cartService.validateCartItem(cartItemId, principal.getName())) { //cartService 에서 검증 로직 발동!
	    	return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
	      }
	      cartService.deleteCartItem(cartItemId);	//상품 수정 로직 호출 다 되면은 삭제
	      return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 응답 리턴
	   }
   
   //장바구니 상품 주문하기
   @PostMapping("/cart/orders")
   public @ResponseBody ResponseEntity<?> deleteCartItem(
	      @RequestBody CartOrderDto cartOrderDto, Principal principal){
	   List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
	  
	   if(cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
		   return new ResponseEntity<String>("주문할 상품을 선택하세요", HttpStatus.BAD_REQUEST);
	   }
	   
	   Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName()); // 상품 주문 + 장바구니에서 주문한거 제거
	   return new ResponseEntity<Long>(orderId, HttpStatus.OK);
   }
   
   
   
}