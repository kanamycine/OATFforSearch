package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionAccountPriorityUpdateFormRequestDto {
	private Long exhibitionAccountId;
	private Integer exhibitionAccountPriority;
}
