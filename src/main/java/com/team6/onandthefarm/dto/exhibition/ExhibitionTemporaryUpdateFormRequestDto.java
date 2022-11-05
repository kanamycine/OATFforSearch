package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionTemporaryUpdateFormRequestDto {
	private Long exhibitionTemporaryId;

	private Long exhibitionTemporaryCategoryId;

	private String exhibitionTemporaryModuleName;

	private Long exhibitionTemporaryDataPicker;

	private Long exhibitionTemporaryAccountId;

	private Long exhibitionTemporaryItemsId;

	private Integer exhibitionTemporaryPriority;

	private Boolean exhibitionActivation;
}
