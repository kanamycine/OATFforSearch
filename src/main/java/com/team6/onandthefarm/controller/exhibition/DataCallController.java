package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.exhibition.DataPickerFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BadgeDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.ProductDataRequestDto;
import com.team6.onandthefarm.entity.exhibition.DataPicker;
import com.team6.onandthefarm.service.exhibition.DataToolService;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.DataPickerFormRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeDataRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerDataRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductDataRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductResponses;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/user/data-picker")
@RequiredArgsConstructor
public class DataCallController {
	private final DataToolService dataToolService;

	@GetMapping(value = "/banner")
	@ApiOperation(value = "배너 데이터 호출")
	public ResponseEntity<BaseResponse<BannerResponses>> getBannerItems(
			@RequestBody BannerDataRequest bannerDataRequest){
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BannerDataRequestDto bannerDataRequestDto = modelMapper.map(bannerDataRequest, BannerDataRequestDto.class);

		BannerResponses bannerResponses = null;

		if (bannerDataRequestDto.getDataToolId().equals(777L)) {
			bannerResponses = dataToolService.getBannerATypeItems(bannerDataRequestDto);
		}

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("BannerResponse gotten")
				.data(bannerResponses)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/product")
	@ApiOperation(value = "상품 데이터 호출")
	public ResponseEntity<BaseResponse<ProductResponses>> getProductItems(@ApiIgnore Principal principal,
			@RequestBody ProductDataRequest productDataRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			if(principalInfo[1].equals("user")){
				userId = Long.parseLong(principalInfo[0]);
			}
		}

		ProductDataRequestDto productDataRequestDto = modelMapper.map(productDataRequest, ProductDataRequestDto.class);

		ProductResponses productResponses = null;

		if(productDataRequestDto.getDataToolId().equals(555L)){
			productResponses = dataToolService.getProductATypeItems(productDataRequestDto, userId);
		}

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ProductResponse gotten")
				.data(productResponses)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/badge")
	@ApiOperation(value = "뱃지 데이터 호출")
	public ResponseEntity<BaseResponse<BadgeResponses>> getBadgeItems(
			@RequestBody BadgeDataRequest badgeDataRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BadgeDataRequestDto badgeDataRequestDto = modelMapper.map(badgeDataRequest, BadgeDataRequestDto.class);

		BadgeResponses badgeResponses = null;

		if(badgeDataRequestDto.getDataToolId().equals(888L)){
			badgeResponses = dataToolService.getBadgeATypeItems(badgeDataRequestDto);
		}

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("BadgeResponse gotten")
				.data(badgeResponses)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
