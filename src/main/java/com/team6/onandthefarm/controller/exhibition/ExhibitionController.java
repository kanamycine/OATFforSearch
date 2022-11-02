package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountDeleteRequest;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountFormRequest;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountUpdateFormRequest;

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

		ExhibitionAccountFormDto exhibitionAccountFormDto = modelMapper.map(exhibitionAccountFormRequest, ExhibitionAccountFormDto.class);

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
				.httpStatus(HttpStatus.CREATED)
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
				.httpStatus(HttpStatus.CREATED)
				.message("ExhibitionAccount DELETED")
				.data(exhibitionAccountId)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}
}
