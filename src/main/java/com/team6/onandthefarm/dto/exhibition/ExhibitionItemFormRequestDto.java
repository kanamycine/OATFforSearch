package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionItemFormRequestDto {
	private Long exhibitionItemId;

	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private Long exhibitionItemProductId;

	private Integer exhibitionItemPriority;

	private String exhibitionItemTime;

	private String exhibitionItemCreatedAt;

	private String exhibitionItemModifiedAt;

	private boolean exhibitionItemStatus;
}
