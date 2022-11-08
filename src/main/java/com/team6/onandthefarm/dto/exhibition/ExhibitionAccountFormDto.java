package com.team6.onandthefarm.dto.exhibition;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionAccountFormDto {
	private Long exhibitionAccountId;

	private Long exhibitionAccountCategoryId;

	private String exhibitionAccountName;

	private String exhibitionAccountStartTime;

	private String exhibitionAccountEndTime;

	private String exhibitionAccountDetail;

	private String exhibitionAccountCreatedAt;

	private String exhibitionAccountModifiedAt;

	private boolean exhibitionAccountStatus;

	private Integer exhibitionAccountPriority;

	private List<ExhibitionItemsFormRequestDto> exhibitionItemsFormRequestDtos;
}
