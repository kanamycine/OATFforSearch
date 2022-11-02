package com.team6.onandthefarm.entity.exhibition;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Module {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "PRODUCT_SEQ_GENERATOR")
	private Long moduleId;

	private String moduleName;

	private String moduleContent;

	private boolean moduleUsableStatus;

	private boolean moduleStatus;

	private String moduleCreatedAt;

	private String moduleModifiedAt;

	private String moduleDevelopCompletedAt;

	private String moduleDevelopModifiedAt;
}
