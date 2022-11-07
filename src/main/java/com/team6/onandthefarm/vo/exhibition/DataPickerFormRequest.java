package com.team6.onandthefarm.vo.exhibition;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

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
public class DataPickerFormRequest {

	private Long exhibitionCategoryId;

	private String dataPickerName;

	private String dataPickerDetail;

	private String dataPickerWriter;
}
