package com.shop.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;


/*
UUID: Universally Unique Identifier
서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용
실제로는 중복될 가능성이 거의 없어서 그냥 파일 이름으로 사용하면 됨
 */

//파일을 처리하는 파일 서비스!
@Log
@Service
public class FileService {
	
	//파일 업로드 메서드
	public String uploadFile(String uploadPath, String originalFileName,
			byte[] fileData) throws IOException {
		//k.abcd.jpg
		UUID uuid = UUID.randomUUID(); //유일한 파일 이름 부여
		String extension = originalFileName.substring(
				originalFileName.lastIndexOf(".")); //확장자
		String savedFileName = uuid.toString() + extension;  //파일 이름을 만듬
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		
		//바이트 단위로 파일 쓰기
		//파일 출력 스트림을 생성함(생성자로 파일이 저장될 위치와 파일 이름을 넘겨줌)
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		
		return savedFileName;
	}

	//파일 삭제
	public void deleteFile(String filePath) {
		//파일이 저장된 경로를 이용하여 파일 객체를 생성함
		File deleteFile = new File(filePath);
		if(deleteFile.exists()) { // 해당 파일이 존재하면은 파일을 삭제
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		}else {
			log.info("파일이 존재하지 않습니다.");
		}
	}

}
