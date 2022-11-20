package com.shop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // @AutoWired 없이 new 해주기
@Service
public class MemberService implements UserDetailsService{
   //MemberService가 UserDetailsService를 구현함.
   private final MemberRepository memberRepo;  //생성자 주입(DI) 방식-객체
   
   //회원 가입(저장)
   public Member saveMember(Member member) {
      validateDuplicateMember(member);  //중복 체크 메서드 호출
      return memberRepo.save(member);
   }
   
   //회원 정보 확인
   public Member view(String email) {
	   return memberRepo.findByName(email);
   }
   
   //이메일 중복 체크 메서드
   private void validateDuplicateMember (Member member) {
      Member findMemberName = memberRepo.findByName(member.getName()); // 이름 체크하고_repository 에 있는 메소드로로
      Member findMemberEmail = memberRepo.findByEmail(member.getEmail()); // 이메일 체크하고_repository 에 있는 메소드로로
      if(findMemberName != null) {
         throw new IllegalStateException("이미 가입된 닉네임입니다."); // IllegalStateException: 이미 가입된 회원이면 메세지 날림
      }
      if(findMemberEmail != null) {
         throw new IllegalStateException("이미 가입된 이메일입니다.");// IllegalStateException: 이미 가입된 회원이면 메세지 날림
      }
   }

   //로그인 할 유저의 email을 파라미터로 전달함
   //User 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role을 파라미터로 넘겨줌
   @Override // loadUserByUsername 오버라이딩
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // user 의 이메일을 전달받는다.(중복x)
      Member member = memberRepo.findByEmail(email);
      
      if(member == null) {
         throw new UsernameNotFoundException(email);
      }
      
      return User.builder()
            .username(member.getName())
            .password(member.getPassword())
            .roles(member.getRole().toString()) // enum 이니까 toString 해준다.
            .build();
   }
   
}