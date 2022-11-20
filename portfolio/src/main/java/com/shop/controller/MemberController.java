package com.shop.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.config.ScriptUtils;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {
   
   private final MemberService memberService;
   
   private final PasswordEncoder pwencoder;
   
   //로그인 폼 요청
   @GetMapping("/login")
   public String loginForm() {
      return "member/loginForm";
   }
   
   //로그인 실패
   @GetMapping("/login/error")
   public String loginFormError(Model model) {
      model.addAttribute("loginErrorMsg", "아이디나 비밀번호를 확인해주세요");
      return "member/loginForm";
   }
   
   //회원 가입 페이지 요청
   @GetMapping("/new")
   public String memberForm(Model model) {
      model.addAttribute("memberFormDto", new MemberFormDto());
      return "member/memberForm";
   // 회원가입 성공 시 메인으로 리다이렉트
   // 실패하면 다시 회원가입 페이지로 돌아감
   }
   
   //회원 가입 처리
   //@Valid - 유효성 검증 어노테이션
   @PostMapping("/new")
   public String memberForm(@Valid MemberFormDto memberFormDto, 
          BindingResult bindingResult , Model model, HttpServletResponse response) throws IOException {
	   // Valid 검증하려는 객체(memberFormDto) 앞에 붙임
       // 검증 완료 되면은 결과를 bindingResult(TF) 에다가 담아줌

       //BindingResult는 검증 오류가 발생할 경우 오류 내용을 보관하는 스프링 프레임워크에서 제공하는 객체입니다.
       // bindingResult.addError 를 해줘야 하지만, DTO 에서 어노테이션으로 처리했기 때문에 페이지 리턴만 해주면 된다!!!
	   
      if(bindingResult.hasErrors()) { // bindingResult.hasErrors 를 호출해서, 에러가 있으면 회원가입 페이지로 return 함
         return "member/memberForm";
      }
      //이메일 중복 처리
      try {
         Member member = Member.createMember(memberFormDto, pwencoder); //member: entity, createMember 에서 dto-> entity
         memberService.saveMember(member); //saveMember: 중복가입막는거 처리해주고 save
         ScriptUtils.alertAndMovePage(response, "회원가입이 정상적으로 완료되었습니다.", "../");
      }catch(IllegalStateException e) {
         model.addAttribute("errorMsg", e.getMessage());
         return "member/memberForm";
      }
      return "redirect:/";
   }
}