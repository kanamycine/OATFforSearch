package com.team6.onandthefarm.dto.exhibition;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExhibitionAccountPriorityUpdateFormsRequestDto {
	private List<ExhibitionAccountPriorityUpdateFormRequestDto> exhibitionAccountPriorityUpdateFormRequestDtos;

}
