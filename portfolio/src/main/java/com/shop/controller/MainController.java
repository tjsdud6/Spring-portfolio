package com.shop.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {

	private final ItemService itemService;
	
	//index페이지
	@GetMapping("/")
	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, 
			Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6); // 처음 페이지(0) 을 보여주고, 한 페이지에 6개 보여줌
		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
		
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "index";
		
	}
	
	
	
}
