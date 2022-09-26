package com.team6.onandthefarm.dto.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;

@Builder
public class MultipartFileDto {
	private MultipartFile productMultipartFile;
	private Long productId;
	private String productImgSrc;
}
