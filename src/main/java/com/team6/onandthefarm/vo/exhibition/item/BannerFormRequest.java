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
public class BannerFormRequest {

	private String bannerName;

	private String bannerConnectUrl;

	private String bannerDetail;

	private List<MultipartFile> bannerImg;
}
