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
public class ExhibitionCategoryResponse {
	private Long exhibitionCategoryId;
	private String exhibitionCategoryName;
}
