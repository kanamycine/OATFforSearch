package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;

public interface ExhibitionService {
	Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto);
	Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto);
	Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto);
}
