package com.team6.onandthefarm.entity.exhibition.item;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
public class ExhibitionBadgeItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long ExhibitionBadgeItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionAccountId")
	private ExhibitionAccount exhibitionAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exhibitionCategoryId")
	private ExhibitionCategory exhibitionCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ExhibitionBadgeId")
	private Badge ExhibitionBadge;

	private String ExhibitionBadgeItemName;

	private Integer ExhibitionBadgeItemPriority;

	private String ExhibitionBadgeItemUrl;

	private String ExhibitionBadgeItemTime;

	private String ExhibitionBadgeItemCreatedAt;

	private String ExhibitionBadgeItemModifiedAt;

	private boolean ExhibitionBadgeItemUsableStatus;

	private boolean ExhibitionBadgeItemStatus;
}
