package com.team6.onandthefarm.vo.exhibition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ExhibitionAccountDeleteRequest {
	private Long exhibitionAccountId;
}
