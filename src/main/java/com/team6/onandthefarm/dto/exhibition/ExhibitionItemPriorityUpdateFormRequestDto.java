package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionItemPriorityUpdateFormRequestDto {
	private Long exhibitionItemId;
	private Integer exhibitionItemPriority;
}
