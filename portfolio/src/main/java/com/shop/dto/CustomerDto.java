package com.shop.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerDto {
   private Long gno;
   private String title;
   private String content;
   private String writer;
   private Long cnt = 0L;
   
}