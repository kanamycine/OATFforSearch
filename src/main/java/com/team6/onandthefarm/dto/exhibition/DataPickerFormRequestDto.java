package com.team6.onandthefarm.dto.exhibition;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataPickerFormRequestDto {
	private Long dataPickerId;

	private Long exhibitionCategoryId;

	private String dataPickerName;

	private String dataPickerDetail;

	private boolean dataPickerStatus;

	private String dataPickerCreatedAt;

	private String dataPickerModifiedAt;

	private String dataPickerWriter;
}
