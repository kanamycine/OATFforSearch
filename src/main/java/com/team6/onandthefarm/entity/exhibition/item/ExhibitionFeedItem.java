package com.team6.onandthefarm.entity.exhibition.item;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class ExhibitionFeedItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long ExhibitionFeedItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionAccountId")
	private ExhibitionAccount exhibitionAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionCategoryId")
	private ExhibitionCategory exhibitionCategory;

	private Long ExhibitionFeedItemFeedId;

	private String ExhibitionFeedItemName;

	private Integer ExhibitionFeedItemPriority;

	private String ExhibitionFeedItemUrl;

	private String ExhibitionFeedItemTime;

	private String ExhibitionFeedItemCreatedAt;

	private String ExhibitionFeedItemModifiedAt;

	private boolean ExhibitionFeedItemUsableStatus;

	private boolean ExhibitionFeedItemStatus;
}
