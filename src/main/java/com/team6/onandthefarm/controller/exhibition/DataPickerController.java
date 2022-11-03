package com.team6.onandthefarm.controller.exhibition;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.exhibition.DataPickerFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.DataPicker;
import com.team6.onandthefarm.service.exhibition.ExhibitionService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.DataPickerFormRequest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/admin/data-picker")
@RequiredArgsConstructor
public class DataPickerController {

	private final ExhibitionService exhibitionService;

	@PostMapping(value="/new")
	@ApiOperation(value="데이터 피커 등록")
	public ResponseEntity<BaseResponse<DataPicker>> createDataPicker(@ApiIgnore Principal principal,
			DataPickerFormRequest dataPickerFormRequest){

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
}
