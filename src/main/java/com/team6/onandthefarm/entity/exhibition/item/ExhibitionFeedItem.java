package com.team6.onandthefarm.entity.exhibition.item;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
		name="EXHIBITION_SEQ_GENERATOR",
		sequenceName = "EXHIBITION_SEQ",
		initialValue = 100000, allocationSize = 1
)
public class ExhibitionFeedItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "EXHIBITION_SEQ_GENERATOR")
	private Long exhibitionFeedItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionAccountId")
	private ExhibitionAccount exhibitionAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionCategoryId")
	private ExhibitionCategory exhibitionCategory;

	private Long exhibitionFeedItemFeedId;

	// private String exhibitionFeedItemName;

	private Integer exhibitionFeedItemPriority;

	// private String exhibitionFeedItemUrl;

	private String exhibitionFeedItemTime;

	private String exhibitionFeedItemCreatedAt;

	private String exhibitionFeedItemModifiedAt;

	private boolean exhibitionFeedItemUsableStatus;

	private boolean exhibitionFeedItemStatus;
}
