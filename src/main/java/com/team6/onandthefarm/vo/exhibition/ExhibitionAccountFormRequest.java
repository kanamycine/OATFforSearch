package com.team6.onandthefarm.vo.exhibition;

import java.util.List;

import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;

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
public class ExhibitionAccountFormRequest {

	private Long exhibitionAccountCategoryId;

	private String exhibitionAccountName;

	private String exhibitionAccountTime;

	private String exhibitionAccountDetail;

	private String exhibitionItemsName;

	private String exhibitionItemsDetail;

	private List<ExhibitionItemFormRequest> exhibitionItemFormRequests;

}
