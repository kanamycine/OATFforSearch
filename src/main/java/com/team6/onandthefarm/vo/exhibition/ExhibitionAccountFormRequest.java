package com.team6.onandthefarm.vo.exhibition;

import java.util.List;

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

	private String exhibitionAccountStartTime;

	private String exhibitionAccountEndTime;

	private String exhibitionAccountDetail;

	private List<ExhibitionItemsFormRequest> exhibitionItemsFormRequests;

}
