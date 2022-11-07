package com.team6.onandthefarm.dto.exhibition;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionTemporaryApplyFormRequestDto {
	private List<Long> exhibitionTemporaryIds;
}
