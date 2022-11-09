package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountPriorityUpdateFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemPriorityUpdateFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemsFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryApplyFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryDeleteFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryPriorityUpdateFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryUpdateFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.Exhibition;
import com.team6.onandthefarm.entity.exhibition.ExhibitionTemporary;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import com.team6.onandthefarm.vo.exhibition.ExhibitionItemPriorityUpdateFormsRequest;
import com.team6.onandthefarm.vo.exhibition.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionItemsFormRequest;
import com.team6.onandthefarm.vo.exhibition.item.ExhibitionTemporaryDeleteFormRequest;

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
			@RequestBody ExhibitionAccountFormRequest exhibitionAccountFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccountFormDto exhibitionAccountFormDto = modelMapper.map(exhibitionAccountFormRequest, ExhibitionAccountFormDto.class);

		List<ExhibitionItemsFormRequest> exhibitionItemsFormRequests = exhibitionAccountFormRequest.getExhibitionItemsFormRequests();
		List<ExhibitionItemsFormRequestDto> exhibitionItemsFormRequestDtos = new ArrayList<>();

		for(ExhibitionItemsFormRequest exhibitionItemsFormRequest : exhibitionItemsFormRequests){
			List<ExhibitionItemFormRequest> exhibitionItemFormRequests = exhibitionItemsFormRequest.getExhibitionItemFormRequests();

			List<ExhibitionItemFormRequestDto> exhibitionItemFormRequestDtos = new ArrayList<>();
			for(ExhibitionItemFormRequest exhibitionItemFormRequest : exhibitionItemFormRequests){
				ExhibitionItemFormRequestDto exhibitionItemFormRequestDto = modelMapper.map(exhibitionItemFormRequest, ExhibitionItemFormRequestDto.class);
				exhibitionItemFormRequestDtos.add(exhibitionItemFormRequestDto);
			}

			ExhibitionItemsFormRequestDto exhibitionItemsFormRequestDto = modelMapper.map(exhibitionItemsFormRequest, ExhibitionItemsFormRequestDto.class);
			exhibitionItemsFormRequestDto.setExhibitionItemFormRequestDtos(exhibitionItemFormRequestDtos);

			exhibitionItemsFormRequestDtos.add(exhibitionItemsFormRequestDto);
		}
		exhibitionAccountFormDto.setExhibitionItemsFormRequestDtos(exhibitionItemsFormRequestDtos);

		Long exhibitionAccountId = exhibitionService.createExhibitionAccount(exhibitionAccountFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("ExhibitionAccount CREATED")
				.data(exhibitionAccountId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/account/update")
	@ApiOperation(value = "전시구좌 수정 / test 없이 진행 추후 필요")
	public ResponseEntity<BaseResponse<ExhibitionAccount>> updateExhibitionAccount(@ApiIgnore Principal principal,
			@RequestBody ExhibitionAccountUpdateFormRequest exhibitionAccountUpdateFormRequest) throws Exception{

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
			@RequestBody ExhibitionAccountDeleteRequest exhibitionAccountDeleteRequest) throws Exception{

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



	@PutMapping(value = "/account/update/priority")
	@ApiOperation(value = "전시 구좌 우선순위 수정")
	public ResponseEntity<BaseResponse<ExhibitionAccount>> updateExhibitionAccountPriority(
			@RequestBody ExhibitionAccountPriorityUpdateFormsRequest exhibitionAccountPriorityUpdateFormsRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccountPriorityUpdateFormsRequestDto exhibitionAccountPriorityUpdateFormsRequestDto =
				modelMapper.map(exhibitionAccountPriorityUpdateFormsRequest, ExhibitionAccountPriorityUpdateFormsRequestDto.class);

		List<ExhibitionAccountPriorityUpdateFormRequestDto> exhibitionAccountPriorityUpdateFormRequestDtos = new ArrayList<>();

		List<ExhibitionAccountPriorityUpdateFormRequest> exhibitionAccountPriorityUpdateFormRequests =
				exhibitionAccountPriorityUpdateFormsRequest.getExhibitionAccountPriorityUpdateFormRequests();

		for (ExhibitionAccountPriorityUpdateFormRequest e : exhibitionAccountPriorityUpdateFormRequests) {
			ExhibitionAccountPriorityUpdateFormRequestDto eDto = modelMapper.map(e, ExhibitionAccountPriorityUpdateFormRequestDto.class);

			exhibitionAccountPriorityUpdateFormRequestDtos.add(eDto);
		}
		exhibitionAccountPriorityUpdateFormsRequestDto.setExhibitionAccountPriorityUpdateFormRequestDtos(exhibitionAccountPriorityUpdateFormRequestDtos);

		List<Long> exhibitionAccountIds = exhibitionService.updateExhibitionAccountPriority(exhibitionAccountPriorityUpdateFormsRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount Priority UPDATED")
				.data(exhibitionAccountIds)
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

	@GetMapping(value = "/account/by-category/{exhibition-category-no}")
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

	@GetMapping(value = "/account/items/{exhibition-account-id}")
	@ApiOperation(value = "전시 구좌 내 소재리스트 불러오기")
	public ResponseEntity<BaseResponse<List<ExhibitionItemsInfoResponse>>> getExhibitionItemsInfos(@ApiIgnore Principal principal,
			@PathVariable("exhibition-account-id") Long exhibitionAccountId){
		List<ExhibitionItemsInfoResponse> exhibitionItemsInfos = exhibitionService.getExhibitionItemsInfos(exhibitionAccountId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionItemsInfo BY ExhibitionAccountId")
				.data(exhibitionItemsInfos)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/account/all-item/{exhibition-items-id}")
	@ApiOperation(value = "전시 소재리스트 내 소재들 불러오기")
	public ResponseEntity<BaseResponse<List<ExhibitionItemInfoResponse>>> getExhibitionItemInfo(@ApiIgnore Principal principal,
			@PathVariable("exhibition-items-id") Long exhibitionItemsId){
		List<ExhibitionItemInfoResponse> exhibitionItemInfos = exhibitionService.getExhibitionItemInfos(exhibitionItemsId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionItemInfos BY ExhibitionItemsId")
				.data(exhibitionItemInfos)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/account/item/update/priority")
	@ApiOperation(value = "소재 아이템 우선순위 수정")
	public ResponseEntity<BaseResponse<ExhibitionItem>> updateExhibitionItemPriority(@ApiIgnore Principal principal,
			@RequestBody ExhibitionItemPriorityUpdateFormsRequest exhibitionItemPriorityUpdateFormsRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionItemPriorityUpdateFormsRequestDto exhibitionItemPriorityUpdateFormsRequestDto = modelMapper.map(
				exhibitionItemPriorityUpdateFormsRequest, ExhibitionItemPriorityUpdateFormsRequestDto.class);
		List<ExhibitionItemPriorityUpdateFormRequestDto> exhibitionItemPriorityUpdateFormRequestDto = new ArrayList<>();

		List<ExhibitionItemPriorityUpdateFormRequest> exhibitionItemPriorityUpdateFormRequests = exhibitionItemPriorityUpdateFormsRequest.getExhibitionItemPriorityUpdateFormRequests();

		for (ExhibitionItemPriorityUpdateFormRequest e : exhibitionItemPriorityUpdateFormRequests) {
			ExhibitionItemPriorityUpdateFormRequestDto eDto =
					modelMapper.map(e, ExhibitionItemPriorityUpdateFormRequestDto.class);
			exhibitionItemPriorityUpdateFormRequestDto.add(eDto);
		}
		exhibitionItemPriorityUpdateFormsRequestDto.setExhibitionItemPriorityUpdateFormRequestDtos(exhibitionItemPriorityUpdateFormRequestDto);

		List<Long> exhibitionItemIds = exhibitionService.updateExhibitionItemPriority(exhibitionItemPriorityUpdateFormsRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionItem Priority UPDATED")
				.data(exhibitionItemIds)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}


	@GetMapping(value = "/account/list/{page-no}")
	@ApiOperation(value = "전시구좌 리스트 조회")
	public ResponseEntity<BaseResponse<ExhibitionSelectionResponseResult>> getAllExhibitionListOrderByNewest(@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber){
		ExhibitionSelectionResponseResult exhibitions = exhibitionService.getAllExhibitionListOrderByNewest(Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount Pagenation")
				.data(exhibitions)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/account/detail/{exhibition-account-id}")
	@ApiOperation(value = "전시구좌 상세보기")
	public ResponseEntity<BaseResponse<ExhibitionAccountDetailResponse>> getExhibitionAccountDetail(@ApiIgnore Principal principal,
																										  @PathVariable("exhibition-account-id") Long exhibitionAccountId){

		ExhibitionAccountDetailResponse exhibitionAccounts  = exhibitionService.getExhibitionAccountDetail(exhibitionAccountId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionAccount BY exhibitionAccountId")
				.data(exhibitionAccounts)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@PostMapping(value ="/temporary/new")
	@ApiOperation(value = "전시 temp 생성 (main view)")
	public ResponseEntity<BaseResponse<ExhibitionTemporary>> createExhibitionTemporary(@ApiIgnore Principal principal,
			@RequestBody ExhibitionTemporaryFormRequest exhibitionTemporaryFormRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporaryFormRequestDto exhibitionTemporaryFormRequestDto = modelMapper.map(exhibitionTemporaryFormRequest, ExhibitionTemporaryFormRequestDto.class);

		Long exhibitionTemporaryId = exhibitionService.createExhibitionTemporary(exhibitionTemporaryFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("ExhibitionTemporary CREATED")
				.data(exhibitionTemporaryId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/temporary/update")
	@ApiOperation(value = "전시 temp 수정 (main view)")
	public ResponseEntity<BaseResponse<ExhibitionTemporary>> updateExhibitionTemporary(@ApiIgnore Principal principal,
			@RequestBody ExhibitionTemporaryUpdateFormRequest exhibitionTemporaryUpdateFormRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporaryUpdateFormRequestDto exhibitionTemporaryUpdateFormRequestDto = modelMapper.map(
				exhibitionTemporaryUpdateFormRequest, ExhibitionTemporaryUpdateFormRequestDto.class);

		Long exhibitionTemporaryId = exhibitionService.updateExhibitionTemporary(exhibitionTemporaryUpdateFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionTemporary UPDATED")
				.data(exhibitionTemporaryId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/temporary/delete")
	@ApiOperation(value = "전시 temp 삭제 (main view)")
	public ResponseEntity<BaseResponse<ExhibitionTemporary>> deleteExhibitionTemporary(@ApiIgnore Principal principal,
			@RequestBody ExhibitionTemporaryDeleteFormRequest exhibitionTemporaryDeleteFormRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporaryDeleteFormRequestDto exhibitionTemporaryDeleteFormRequestDto = modelMapper.map(exhibitionTemporaryDeleteFormRequest, ExhibitionTemporaryDeleteFormRequestDto.class);

		Long exhibitionTemporaryId = exhibitionService.deleteExhibitionTemporary(exhibitionTemporaryDeleteFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionTemporary DELETED")
				.data(exhibitionTemporaryId)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/temporary/all")
	@ApiOperation(value = "전시 temp 전부 조회(main view)")
	public ResponseEntity<BaseResponse<ExhibitionTemporaryAllResponse>> getAllExhibitionTemporary(@ApiIgnore Principal principal){
		List<ExhibitionTemporaryAllResponse> exhibitionTemporaries = exhibitionService.getAllExhibitionTemporary();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionTemporaryAll")
				.data(exhibitionTemporaries)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/all")
	@ApiOperation(value = "전시 전부 조회(main view not temp)")
	public ResponseEntity<BaseResponse<ExhibitionAllResponse>> getAllExhibition(){
		List<ExhibitionAllResponse> exhibitions = exhibitionService.getAllExhibition();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("Exhibition All")
				.data(exhibitions)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/temporary/apply")
	@ApiOperation(value = "전시 temp 적용 (main view)")
	public ResponseEntity<BaseResponse<Exhibition>> applyExhibitionTemporary(@ApiIgnore Principal principal,
			@RequestBody ExhibitionTemporaryApplyFormRequest exhibitionTemporaryApplyFormRequest){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporaryApplyFormRequestDto exhibitionTemporaryApplyFormRequestDto = modelMapper.map(exhibitionTemporaryApplyFormRequest, ExhibitionTemporaryApplyFormRequestDto.class);

		List<Long> exhibitionIds = exhibitionService.applyExhibitionTemporary(exhibitionTemporaryApplyFormRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionTemporary APPLIED")
				.data(exhibitionIds)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/temporary/update/priority")
	@ApiOperation(value = "임시 전시 우선순위 수정")
	public ResponseEntity<BaseResponse<ExhibitionTemporary>> updateExhibitionTemporaryPriority(
			@RequestBody ExhibitionTemporaryPriorityUpdateFormsRequest exhibitionTemporaryUpdatePriorityFormRequest){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporaryPriorityUpdateFormsRequestDto exhibitionTemporaryPriorityUpdateFormsRequestDto = modelMapper.map(exhibitionTemporaryUpdatePriorityFormRequest, ExhibitionTemporaryPriorityUpdateFormsRequestDto.class);

		List<ExhibitionTemporaryPriorityUpdateFormRequest> exhibitionTemporaryPriorityUpdateFormRequests =
				exhibitionTemporaryUpdatePriorityFormRequest.getExhibitionTemporaryPriorityUpdateFormRequests();
		List<ExhibitionTemporaryPriorityUpdateFormRequestDto> exhibitionTemporaryPriorityUpdateFormRequestDtos = new ArrayList<>();
		for (ExhibitionTemporaryPriorityUpdateFormRequest e : exhibitionTemporaryPriorityUpdateFormRequests) {
			ExhibitionTemporaryPriorityUpdateFormRequestDto eDto = modelMapper.map(e, ExhibitionTemporaryPriorityUpdateFormRequestDto.class);
			exhibitionTemporaryPriorityUpdateFormRequestDtos.add(eDto);
		}
		exhibitionTemporaryPriorityUpdateFormsRequestDto.setExhibitionTemporaryPriorityUpdateFormRequests(exhibitionTemporaryPriorityUpdateFormRequestDtos);

		List<Long> exhibitionTemporaryIds = exhibitionService.updateExhibitionTemporaryPriority(exhibitionTemporaryPriorityUpdateFormsRequestDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("ExhibitionTemporary Priority UPDATED")
				.data(exhibitionTemporaryIds)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}



}
