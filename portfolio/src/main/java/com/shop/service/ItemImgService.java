package com.shop.service;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;

//상품 이미지를 업로드하고, 상품 이미지 정보를 저장
@Transactional
@RequiredArgsConstructor
@Service
public class ItemImgService {
	
	@Value("${itemImgLocation}") // application.properties 에 적었던 itemImgLocation 값을 불러와서 itemImgLocation 변수에다가 넣어줌
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepo;
	
	private final FileService fileService;
	
	//이미지 저장 메서드
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {
		// 일단 비어있는 변수 만들어두고 밑에서 넣어줌
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			// 상품 이미지 이름 = 저장할 경로 + 파일이름 + 파일크기(byte)
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, 
					itemImgFile.getBytes());
			
			// 저장한 상품 이미지를 불러올 경로
			imgUrl = "/images/item/" + imgName;
		}
		
		//상품 이미지 정보
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepo.save(itemImg);  //이미지 정보 및 파일 저장
		/*
        imgName: 실제 로컬에 저장된 상품 이미지 파일 이름
        oriImgName: 업로드했던 상품 이미지 파일 초기 이름
        imgUrl: 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러올 경로
         */
	}	
	
	//이미지 수정 메소드
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws IOException {
		if(!itemImgFile.isEmpty()) { // 상품 이미지를 수정한 경우, 상품 이미지를 업데이트함
			// 상품 이미지 아이디를 이용해서 기존에 저장했던 상품 이미지 엔티티를 조회
			ItemImg savedItemImg = itemImgRepo.findById(itemImgId) 
					.orElseThrow(EntityNotFoundException::new);
		
			//기존 이미지 파일을 삭제
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); // 업데이트한 상품 이미지 파일을 업로드
			}
			
			//수정한 이미지 파일 업로드
	         String oriImgName = itemImgFile.getOriginalFilename();
	         String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
	         
	         String imgUrl = "/images/item/" + imgName;
	         savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
	         /*
	            처음 이미지 등록할 때는 itemImgRepository.save(itemImg); 이거를 썼자나
	            근데 updateItemImg 을 쓰는 이유는
	            savedItemImg 엔티티는 현재 영속 상태임. 데이터 변경하는거로도 변경 감지 기능이 동작해서!!! transaction 이 끝날 때 update 쿼리가 실행되기 때문
	            (중요한건 엔티티가 영속 상태라는 것!)
	             */
	      }

		
	}
	
}
