package com.team6.onandthefarm.dto.exhibition;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionTemporaryPriorityUpdateFormRequestDto {
	private Long exhibitionTemporaryId;
	private Integer exhibitionTemporaryPriority;
}
