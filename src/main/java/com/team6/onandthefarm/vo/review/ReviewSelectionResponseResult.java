package com.team6.onandthefarm.vo.review;

import java.util.List;

import com.team6.onandthefarm.vo.PageVo;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;

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
public class ReviewSelectionResponseResult {
	private List<ReviewSelectionResponse> reviewSelectionResponses;
	private PageVo pageVo;
}
