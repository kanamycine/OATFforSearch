package com.team6.onandthefarm.entity.exhibition.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Banner {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long bannerId;

	private String bannerName;

	private String bannerMainImgSrc;

	private String bannerDetail;

	private String bannerCreatedAt;

	private String bannerModifiedAt;

	private boolean bannerStatus;

	private String bannerConnectUrl;
}
