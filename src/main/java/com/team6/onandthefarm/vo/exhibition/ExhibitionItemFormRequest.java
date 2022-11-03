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
public class ExhibitionItemFormRequest {
	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private Long exhibitionItemId;

	private Integer exhibitionItemPriority;

	private String exhibitionItemTime;
}
