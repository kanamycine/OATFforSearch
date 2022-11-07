package com.team6.onandthefarm.dto.exhibition;

import java.util.List;

import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionItemsFormRequestDto {
	private Long exhibitionItemsId;

	private Long exhibitionAccountId;

	private String exhibitionItemsName;

	private String exhibitionItemsDetail;

	private List<ExhibitionItemFormRequestDto> exhibitionItemFormRequestDtos;
}
