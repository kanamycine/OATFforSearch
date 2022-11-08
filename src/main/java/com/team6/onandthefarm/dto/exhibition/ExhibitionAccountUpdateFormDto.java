package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionAccountUpdateFormDto {
	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private String exhibitionAccountName;

	private String exhibitionAccountStartTime;

	private String exhibitionAccountEndTime;

	private String exhibitionAccountDetail;

	private String exhibitionAccountCreatedAt;

	private String exhibitionAccountModifiedAt;

	private boolean exhibitionAccountStatus;

	private Integer exhibitionAccountPriority;
}
