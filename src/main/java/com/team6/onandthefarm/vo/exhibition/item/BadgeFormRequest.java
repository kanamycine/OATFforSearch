package com.team6.onandthefarm.vo.exhibition.item;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeFormRequest {
	private String badgeName;

	private String badgeDetail;

	private String badgeConnectUrl;

	private List<MultipartFile> badgeImg;
}
