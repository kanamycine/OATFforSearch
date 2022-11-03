package com.team6.onandthefarm.dto.exhibition.item;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BannerFormRequestDto {
	private Long bannerId;

	private String bannerName;

	private String bannerDetail;

	private String bannerConnectUrl;

	private String bannerCreatedAt;

	private String bannerModifiedAt;

	private boolean bannerStatus;

	private List<MultipartFile> bannerImg;
}
