package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionItemFormRequestDto {
	private Long exhibitionItemId;

	private Long exhibitionItemsId;

	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private Long exhibitionItemNumber;

	private Integer exhibitionItemPriority;

	private String exhibitionItemStartTime;

	private String exhibitionItemEndTime;

	private String exhibitionItemCreatedAt;

	private String exhibitionItemModifiedAt;

	private boolean exhibitionItemStatus;
}
