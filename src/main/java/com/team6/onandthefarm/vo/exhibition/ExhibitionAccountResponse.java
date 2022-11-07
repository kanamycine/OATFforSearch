package com.team6.onandthefarm.vo.exhibition;

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
public class ExhibitionAccountResponse {
	private Long exhibitionAccountId;
	private String exhibitionAccountName;
	private boolean exhibitionAccountStatus; // 삭제된건 보내지마라
}
