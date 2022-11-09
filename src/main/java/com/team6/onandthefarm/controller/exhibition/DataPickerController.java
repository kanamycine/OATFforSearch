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
import com.team6.onandthefarm.dto.exhibition.datatool.BadgeATypeRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerATypeRequestDto;
import com.team6.onandthefarm.entity.exhibition.DataPicker;
import com.team6.onandthefarm.service.exhibition.DataToolService;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.DataPickerFormRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeRequest;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerResponses;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/admin/data-picker")
@RequiredArgsConstructor
public class DataPickerController {

	private final ExhibitionService exhibitionService;
	private final DataToolService dataToolService;

	@PostMapping(value="/new")
	@ApiOperation(value="데이터 피커 등록")
	public ResponseEntity<BaseResponse<DataPicker>> createDataPicker(@ApiIgnore Principal principal,
			@RequestBody DataPickerFormRequest dataPickerFormRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		DataPickerFormRequestDto dataPickerFormRequestDto = modelMapper.map(dataPickerFormRequest, DataPickerFormRequestDto.class);

		Long dataPickerId = exhibitionService.createDataPicker(dataPickerFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("DataPicker CREATED")
				.data(dataPickerId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@GetMapping(value = "/banner")
	@ApiOperation(value = "배너 데이터 호출")
	public ResponseEntity<BaseResponse<BannerResponses>> getBannerItems(
			@RequestBody BannerATypeRequest bannerAtypeRequest){
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BannerATypeRequestDto bannerAtypeRequestDto = modelMapper.map(bannerAtypeRequest, BannerATypeRequestDto.class);

		BannerResponses bannerResponses = null;

		if (bannerAtypeRequestDto.getDataToolId().equals(777L)) {
			bannerResponses = dataToolService.getBannerATypeItems(bannerAtypeRequestDto);
		}

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("BannerResponse gotten")
				.data(bannerResponses)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/badge")
	@ApiOperation(value = "뱃지 데이터 호출")
	public ResponseEntity<BaseResponse<BadgeResponses>> getBadgeItems(
			@RequestBody BadgeATypeRequest badgeATypeRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		BadgeATypeRequestDto badgeATypeRequestDto = modelMapper.map(badgeATypeRequest, BadgeATypeRequestDto.class);

		BadgeResponses badgeResponses = null;

		if(badgeATypeRequestDto.getDataToolId().equals(888L)){
			badgeResponses = dataToolService.getBadgeATypeItems(badgeATypeRequestDto);
		}

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("BadgeResponse gotten")
				.data(badgeResponses)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
