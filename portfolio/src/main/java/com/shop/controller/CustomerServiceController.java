package com.shop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.dto.CustomerDto;
import com.shop.dto.NoticeDto;
import com.shop.dto.PageRequestDto;
import com.shop.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/customer")
@Controller
public class CustomerServiceController {

   private final CustomerService service;
   
   @GetMapping("/list")
   public String list(PageRequestDto pageRequestDto, Model model) {
      model.addAttribute("result", service.getList(pageRequestDto));
      return "/customer/list";
   }
   
   //글쓰기 폼 요청
   @GetMapping("/register")
   public void register() {}
   
   //글쓰기 처리
   @PostMapping("/register")
   public String register(CustomerDto dto, Model model, RedirectAttributes redirectAttributes,@AuthenticationPrincipal CustomerDto customerDto) {
      Long gno = service.register(dto);
      redirectAttributes.addFlashAttribute("msg", gno);
      model.addAttribute("customer", customerDto);
      return "redirect:list";
   }
   
   //상세보기
   @GetMapping("/read")
   public void read(Long gno, Model model , 
         @ModelAttribute("requestDto") PageRequestDto requestDto) {
	   //조회수 증가
	   service.updateCount(gno);
	   
      CustomerDto dto = service.read(gno);   //게시글 1개
      model.addAttribute("dto", dto);
   }
   
 //글 삭제
   @GetMapping("/delete")
   public String delete(Long gno) {
	   service.remove(gno);
	   return "redirect:list";
   }
   
   //글 수정
   @PostMapping("update")
   public String modify(CustomerDto customerDto) {
	   service.modify(customerDto);
	   return "redirect:list";
   }
   
   
}