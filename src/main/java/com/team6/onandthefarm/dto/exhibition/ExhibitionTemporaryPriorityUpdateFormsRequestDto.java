package com.team6.onandthefarm.dto.exhibition;

import java.util.List;

import com.team6.onandthefarm.vo.exhibition.ExhibitionTemporaryPriorityUpdateFormRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionTemporaryPriorityUpdateFormsRequestDto {
	private List<ExhibitionTemporaryPriorityUpdateFormRequestDto> exhibitionTemporaryPriorityUpdateFormRequests;
}
