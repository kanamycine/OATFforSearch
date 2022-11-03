package com.team6.onandthefarm.entity.exhibition.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
public class Badge {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "EXHIBITION_SEQ_GENERATOR")
	private Long badgeId;

	private String badgeName;

	private String badgeMainImgSrc;

	private String badgeDetail;

	private String badgeConnectUrl;

	private String badgeCreatedAt;

	private String badgeModifiedAt;

	private boolean badgeStatus;


	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "exhibitionAccountId")
	// private ExhibitionAccount badgeExhibitionAccount;
	//
	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "dataPickerId")
	// private DataPicker badgeDataPicker;
}
