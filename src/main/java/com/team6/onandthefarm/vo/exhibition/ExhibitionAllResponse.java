package com.team6.onandthefarm.vo.exhibition;

import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

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
public class ExhibitionAllResponse {
	private Long exhibitionId;

	private Long exhibitionCategoryId;

	private String exhibitionModuleName;

	private Long exhibitionDataPickerId;

	private Long exhibitionAccountId;

	private Long exhibitionItemsId;

	private Integer exhibitionPriority;

	private Boolean exhibitionActivation;
}
