package com.shop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {
   
   private final ItemService itemService;
   
   //상품 등록 페이지 요청
   @GetMapping("/admin/item/new")
   public String itemForm(Model model) {
      model.addAttribute("itemFormDto", new ItemFormDto());
      return "item/itemForm";
   }
   
   //상품 등록 처리 요청
   @PostMapping("/admin/item/new")
   public String itemNew(@Valid ItemFormDto itemFormDto, 
         BindingResult bindingResult, Model model,
         @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) throws IOException {
	// 상품 등록 시 필수 값이 없을 때 애러 발생
      if(bindingResult.hasErrors()) {
         return "item/itemForm"; // 에러가 발생하면 상품 등록 get 페이지로 이동
      }
      
   // 상품 등록 시 첫번째 이미지가 없으면 애러 발생 (첫 번째 이미지는 대표 상품 이미지여서 꼭 있어야함!)
      if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
         model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
         return "item/itemForm"; // 에러가 발생하면 상품 등록 get 페이지로 이동
      }
      
      //상품 저장
      try {
         itemService.saveItem(itemFormDto, itemImgFileList);  // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
      }catch(Exception e) {
         model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
         return "item/itemForm";
      }
      return "redirect:/";
   }
   
   //상품 상세 보기
   @GetMapping("/admin/item/{itemId}")
   public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
      try {
         ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
         model.addAttribute("itemFormDto", itemFormDto); // 조회한 상품 데이터를 model 에 담아서 뷰로 전달함
      }catch(Exception e) { // 상품 엔티티가 존재하지 않으면은 에러메세지 + 상품 등록페이지로 다시 이동
         model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
         model.addAttribute("itemFormDto", new ItemFormDto());
         return "item/itemForm";
      }
      
      return "item/itemForm";
   }
   
   //상품 수정 처리
   @PostMapping("/admin/item/{itemId}")
   public String itemUpdate(@Valid ItemFormDto itemFormDto,
         BindingResult bindingResult, Model model,
         @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
      System.out.println(Objects.isNull(itemFormDto.getItemImgDtoList()));
  		// 상품 수정 시 필수 값이 없을 때 애러 발생 
      if(bindingResult.hasErrors()) {
         return "item/itemForm"; // 에러가 발생하면 상품 수정 get 페이지로 이동
      }
      
      // 상품 수정 시 첫번째 이미지가 없으면 애러 발생 (첫 번째 이미지는 대표 상품 이미지여서 꼭 있어야함!) 
      if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
         model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
         return "item/itemForm"; // 에러가 발생하면 상품 수정 get 페이지로 이동
      }
      
      //상품 수정
      try {
         itemService.updateItem(itemFormDto, itemImgFileList); // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
      }catch(Exception e) {
         model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
         return "item/itemForm";
      }
      return "redirect:/"; // 메인 페이지로 리다이렉트
   }
   
   //상품 검색 및 페이지
   //상품 관리 페이지 진입시 페이지 번호가 없는 경우와 있는 경우
   @GetMapping({"/admin/items", "/admin/items/{page}"}) // url 에 페이지 번호가 없는거랑, 페이지 번호가 있는거 둘 다 매핑해줌
   public String itemManage(ItemSearchDto itemSearchDto, 
         @PathVariable("page") Optional<Integer> page, Model model) {
      //3을 페이지당 자료의 개수
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
      //상품 검색 및 페이지 메서드 호출
      Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
      
      model.addAttribute("items", items);  //items를 뷰로 전달
      model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건을 유지한 채 이동
      model.addAttribute("maxPage", 5);  //화면 하단에 보여줄 페이지 번호의 개수(1 2 3 4 5)
      
      return "item/itemMng";
   }
   
   //상품 상세보기(회원 - 주문하기)
   @GetMapping("/item/{itemId}")
   public String itemDtl(Model model, @PathVariable("itemId") Long ItemId) {
	// getItemDtl: service 에 있는 메소드. 상품이랑, 상품이미지의 entity -> dto 로 바꾸기만 하는 service
      ItemFormDto itemFormDto = itemService.getItemDtl(ItemId);
      model.addAttribute("item",itemFormDto);
      return "item/itemDtl";
   }
}