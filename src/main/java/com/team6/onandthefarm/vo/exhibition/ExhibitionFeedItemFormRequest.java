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
public class ExhibitionFeedItemFormRequest {
	private Long exhibitionFeedItemId;

	private Long exhibitionAccountId;

	private Long exhibitionCategoryId;

	private Long exhibitionFeedItemFeedId;

	// private String exhibitionFeedItemName;

	private Integer exhibitionFeedItemPriority;

	private String exhibitionFeedItemUrl;

	private String exhibitionFeedItemTime;
}
