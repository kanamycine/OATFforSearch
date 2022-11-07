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
public class ExhibitionAccountUpdateFormRequest {
	private Long exhibitionAccountId;

	private Long exhibitionAccountCategoryId;

	private String exhibitionAccountName;

	private String exhibitionAccountStartTime;

	private String exhibitionAccountEndTime;

	// private boolean exhibitionAccountUsableStatus;

	private boolean exhibitionAccountStatus;
}
