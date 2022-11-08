package com.team6.onandthefarm.vo.exhibition;

import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;

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
public class ExhibitionSelectionResponse {
		private Long exhibitionAccountId;
		private Long exhibitionCategoryId;
		private String exhibitionCategoryName;
		private String exhibitionAccountName;
		private String exhibitionAccountStartTime;
		private String exhibitionAccountEndTime;

	public ExhibitionSelectionResponse(ExhibitionAccount exhibitionAccount) {
		this.exhibitionAccountId = exhibitionAccount.getExhibitionAccountId();
		this.exhibitionCategoryId = exhibitionAccount.getExhibitionCategory().getExhibitionCategoryId();
		this.exhibitionCategoryName = exhibitionAccount.getExhibitionCategory().getExhibitionCategoryName();
		this.exhibitionAccountName = exhibitionAccount.getExhibitionAccountName();
		this.exhibitionAccountStartTime = exhibitionAccount.getExhibitionAccountStartTime();
		this.exhibitionAccountEndTime = exhibitionAccount.getExhibitionAccountEndTime();
	}
}
