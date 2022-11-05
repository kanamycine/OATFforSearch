package com.team6.onandthefarm.dto.exhibition;

import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionTemporaryFormRequestDto {

	private Long exhibitionTemporaryId;

	private Long exhibitionTemporaryCategoryId;

	private String exhibitionTemporaryModuleName;

	private Long exhibitionTemporaryDataPicker;

	private Long exhibitionTemporaryAccountId;

	private Long exhibitionTemporaryItemsId;

	private Integer exhibitionTemporaryPriority;

	private Boolean exhibitionActivation;
}
