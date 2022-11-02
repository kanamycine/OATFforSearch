package com.team6.onandthefarm.service.exhibition;

import java.util.List;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountResponse;

public interface ExhibitionService {
	Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto);
	Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto);
	Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto);
	List<ExhibitionAccountResponse> getExhibitionAccountByExhibitionCategory(Long exhibitionCategoryId);
}
