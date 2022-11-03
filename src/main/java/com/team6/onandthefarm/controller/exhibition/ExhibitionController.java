package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.service.exhibition.ExhibitionItemService;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountDeleteRequest;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountFormRequest;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountUpdateFormRequest;
import com.team6.onandthefarm.vo.exhibition.ExhibitionCategoryResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionItemFormRequest;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/admin/exhibition")
@RequiredArgsConstructor
public class ExhibitionController {

	private final ExhibitionService exhibitionService;

	@PostMapping(value = "/account/new")
	@ApiOperation(value = "전시구좌 추가")
	public ResponseEntity<BaseResponse<ExhibitionAccount>> createExhibitionAccount(@ApiIgnore Principal principal,
			ExhibitionAccountFormRequest exhibitionAccountFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		List<ExhibitionItemFormRequest> exhibitionItemFormRequests = exhibitionAccountFormRequest.getExhibitionItemFormRequests();
		List<ExhibitionItemFormRequestDto> exhibitionItemFormRequestDtos = new ArrayList<>();

		ExhibitionAccountFormDto exhibitionAccountFormDto = modelMapper.map(exhibitionAccountFormRequest, ExhibitionAccountFormDto.class);

		for (ExhibitionItemFormRequest exhibitionItemFormRequest : exhibitionItemFormRequests) {
			ExhibitionItemFormRequestDto exhibitionItemFormRequestDto = modelMapper.map(exhibitionItemFormRequest, ExhibitionItemFormRequestDto.class);
			exhibitionItemFormRequestDtos.add(exhibitionItemFormRequestDto);
		}

		exhibitionAccountFormDto.setExhibitionItemFormRequestDtos(exhibitionItemFormRequestDtos);

		Long exhibitionAccountId = exhibitionService.createExhibitionAccount(exhibitionAccountFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("ExhibitionAccount CREATED")
				.data(exhibitionAccountId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/account/update")
	@ApiOperation(value = "전시구좌 수정")
	public ResponseEntity<BaseResponse<ExhibitionAccount>> updateExhibitionAccount(@ApiIgnore Principal principal,
			ExhibitionAccountUpdateFormRequest exhibitionAccountUpdateFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto = modelMapper.map(exhibitionAccountUpdateFormRequest, ExhibitionAccountUpdateFormDto.class);

		Long exhibitionAccountId = exhibitionService.updateExhibitionAccount(exhibitionAccountUpdateFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount UPDATED")
				.data(exhibitionAccountId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/account/delete")
	@ApiOperation(value = "전시구좌 삭제")
	public ResponseEntity<BaseResponse<ExhibitionAccount>> deleteExhibitionAccount(@ApiIgnore Principal principal,
			ExhibitionAccountDeleteRequest exhibitionAccountDeleteRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccountDeleteDto exhibitionAccountDeleteDto = modelMapper.map(exhibitionAccountDeleteRequest, ExhibitionAccountDeleteDto.class);

		Long exhibitionAccountId = exhibitionService.deleteExhibitionAccount(exhibitionAccountDeleteDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount DELETED")
				.data(exhibitionAccountId)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/category")
	@ApiOperation(value = "전시 카테고리 전체 조회 - 카테고리 선택시 사용")
	public ResponseEntity<BaseResponse<List<ExhibitionCategoryResponse>>> getAllExhibitionCategory(@ApiIgnore Principal principal){

		List<ExhibitionCategoryResponse> exhibitionCategories = exhibitionService.getAllExhibitionCategory();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionCategory ALL")
				.data(exhibitionCategories)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);

	}

	@GetMapping(value = "/account/{exhibition-category-no}")
	@ApiOperation(value = "전시카테고리 별 전시구좌 조회 - 전시구좌 선택시 사용")
	public ResponseEntity<BaseResponse<List<ExhibitionAccountResponse>>> getExhibitionAccountByExhibitionCategory(@ApiIgnore Principal principal,
			@PathVariable("exhibition-category-no") Long exhibitionCategoryId){
		List<ExhibitionAccountResponse> exhibitionAccounts  = exhibitionService.getExhibitionAccountByExhibitionCategory(exhibitionCategoryId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount BY ExhibitionCategoryId")
				.data(exhibitionAccounts)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	// @PostMapping(value = "/items/new")
	// @ApiOperation(value = "전시 상품 소재들 등록")
	// public ResponseEntity<BaseResponse<ExhibitionItems>> createExhibitionProductItem(@ApiIgnore Principal principal,
	// 		ExhibitionItemFormRequest exhibitionItemFormRequest){
	// 	ModelMapper modelMapper = new ModelMapper();
	// 	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	//
	// 	ExhibitionItemFormRequestDto exhibitionItemFormRequestDto = modelMapper.map(exhibitionItemFormRequest, ExhibitionProductItemFormRequestDto.class);
	//
	// 	Long exhibitionItemId = exhibitionItemService.createExhibitionItem(exhibitionItemFormRequestDto);
	//
	// 	BaseResponse baseResponse = BaseResponse.builder()
	// 			.httpStatus(HttpStatus.CREATED)
	// 			.message("ExhibitionProductItem CREATED")
	// 			.data(exhibitionProductItemId)
	// 			.build();
	//
	// 	return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	// }

}
