   package com.shop.controller;
   
   import java.security.Principal;
   import java.util.List;
   import java.util.Optional;
   
   import javax.validation.Valid;
   
   import org.springframework.data.domain.Page;
   import org.springframework.data.domain.PageRequest;
   import org.springframework.data.domain.Pageable;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
   import org.springframework.validation.FieldError;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   import org.springframework.web.bind.annotation.ResponseBody;
   
   import com.shop.dto.OrderDto;
   import com.shop.dto.OrderHistDto;
   import com.shop.service.OrderService;
   
   import lombok.RequiredArgsConstructor;
   
// 주문 관련 요청들을 처리
// 비동기 방식 사용_상품 주문에서 웹 페이지의 새로 고침 없이 서버에서 주문을 요청하기 위해서
   @RequiredArgsConstructor
   @Controller
   public class OrderController {
   
      private final OrderService orderService;
      
      //주문하기
      @PostMapping("/order")
      public @ResponseBody ResponseEntity<?> order(
            @RequestBody @Valid OrderDto orderDto, 
            BindingResult bindingResult, Principal principal){
    	  // @ResponseBody: Http 요청의 본문 body 에 담긴 내용을 자바 객체로 전달
          // @RequestBody: 자바 객체를 HTTP 요청의 body 로 전달
          // Principal: 파라미터로 넘어온 principal 객체에 데이터를 넣으면, 해당 객체에 직접 접근할 수 있다. (@Controller 가 선언되어있어야함)
    	  
    	  
         //유효성 검증
         if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors) {
               sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
         // 에러 정보는 ResponseEntity 객체에 담겨져서 전달됨. 에러정보는 entity
         }
         
         // 현재 로그인한 회원의 이메일 정보 (로그인 유저 정보 얻을려고)
         String name = principal.getName();   // principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회함
         //String email = principal.getName();   //로그인한 회원
         Long orderId;   // 주문번호 
         
         try {
         orderId = orderService.order(orderDto, name);   //주문 로직 호출
         }catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
         
         //결과값으로 생성된 주문 번호와 요청이 성공했다는 http 응답 상태 코드를 반환
         return new ResponseEntity<Long>(orderId, HttpStatus.OK);
      }
      
      //주문내역
      @GetMapping({"/orders", "/orders/{page}"})
      public String orderHist(@PathVariable("page") Optional<Integer> page,
            Principal principal, Model model) {
         Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 4);
         
         // 현재 로그인한 회원은 화면에 전달한 주문 목록 데이터를 리턴 값으로 받음 (이메일과 페이징 객체를 파라미터로 전달)
         Page<OrderHistDto> orderHistDtoList = 
               orderService.getOrderList(principal.getName(), pageable); //주문 
         // ordersHistDtoList: 주문 목록 데이터
         // principal.getName(): 이메일(현재 로그인한 회원)
         // pageable: 페이징 객체?
         
         
         model.addAttribute("orders",orderHistDtoList);
         model.addAttribute("page", pageable.getPageNumber());
         model.addAttribute("maxPage", 5);
         
         return "order/orderHist";
      }
      
      //주문 취소
      // @ResponseBody 는 return 을 json 처럼 되게 해줌 == @RestController
      @PostMapping("/order/{orderId}/cancel")
      public @ResponseBody ResponseEntity<?> cancelOrder(
            @PathVariable("orderId") Long orderId, Principal principal){
         
         if(!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다", HttpStatus.FORBIDDEN);
         }
         
         orderService.cancelOrder(orderId);
         return new ResponseEntity<Long>(orderId, HttpStatus.OK);
      }
      
   }