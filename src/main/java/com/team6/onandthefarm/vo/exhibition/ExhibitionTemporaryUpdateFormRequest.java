package com.team6.onandthefarm.vo.exhibition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionTemporaryUpdateFormRequest {
	private Long exhibitionTemporaryCategoryId;

	private String exhibitionTemporaryModuleName;

	private Long exhibitionTemporaryDataPicker;

	private Long exhibitionTemporaryAccountId;

	private Long exhibitionTemporaryItemsId;

	private Integer exhibitionTemporaryPriority;
}
