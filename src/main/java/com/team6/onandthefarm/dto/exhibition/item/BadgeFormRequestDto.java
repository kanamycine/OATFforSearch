package com.team6.onandthefarm.dto.exhibition.item;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BadgeFormRequestDto {
	private Long badgeId;

	private String badgeName;

	private String badgeDetail;

	private String badgeConnectUrl;

	private String badgeCreatedAt;

	private String badgeModifiedAt;

	private boolean badgeStatus;

	private List<MultipartFile> badgeImg;
}
