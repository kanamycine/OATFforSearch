package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team6.onandthefarm.dto.exhibition.item.BadgeFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.item.BannerFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.item.Badge;
import com.team6.onandthefarm.entity.exhibition.item.Banner;
import com.team6.onandthefarm.service.exhibition.ExhibitionItemService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.item.BadgeFormRequest;
import com.team6.onandthefarm.vo.exhibition.item.BannerFormRequest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/admin/item")
@RequiredArgsConstructor
public class ItemController {

	private final ExhibitionItemService exhibitionItemService;

	@PostMapping(value = "/new/banner")
	@ApiOperation(value = "배너 등록")
	public ResponseEntity<BaseResponse<Banner>> createBanner(
			@ApiIgnore Principal principal,
			@RequestPart(value = "images", required = false) List<MultipartFile> photo,
			@RequestPart(value = "data", required = false) BannerFormRequest bannerFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BannerFormRequestDto bannerFormRequestDto = modelMapper.map(bannerFormRequest, BannerFormRequestDto.class);

		bannerFormRequestDto.setBannerImg(photo);

		Long bannerId = exhibitionItemService.createBanner(bannerFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("Banner CREATED")
				.data(bannerId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	public ResponseEntity<BaseResponse<Badge>> createBadge(
			@ApiIgnore Principal principal,
			@RequestPart(value = "images", required = false) List<MultipartFile> photo,
			@RequestPart(value = "data", required = false) BadgeFormRequest badgeFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BadgeFormRequestDto badgeFormRequestDto = modelMapper.map(badgeFormRequest, BadgeFormRequestDto.class);

		badgeFormRequestDto.setBadgeImg(photo);

		Long badgeId = exhibitionItemService.createBadge(badgeFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("Badge CREATED")
				.data(badgeId)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
	}
}
