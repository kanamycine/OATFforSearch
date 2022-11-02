package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionAccountUpdateFormDto {
	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private String exhibitionAccountName;

	private String exhibitionAccountTime;

	private String exhibitionAccountCreatedAt;

	private String exhibitionAccountModifiedAt;

	private boolean exhibitionAccountUsableStatus;

	private boolean exhibitionAccountStatus;
}
