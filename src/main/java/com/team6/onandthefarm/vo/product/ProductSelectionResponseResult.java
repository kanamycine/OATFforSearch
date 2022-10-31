package com.team6.onandthefarm.vo.product;

import java.util.List;

import com.team6.onandthefarm.vo.PageVo;

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
public class ProductSelectionResponseResult {
	private List<ProductSelectionResponse> productSelectionResponses;
	private PageVo pageVo;
}
