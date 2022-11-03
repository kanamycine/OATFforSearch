package com.team6.onandthefarm.service.exhibition;

import java.util.List;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionCategoryResponse;

public interface ExhibitionService {
	Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto);
	Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto);
	Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto);
	List<ExhibitionCategoryResponse> getAllExhibitionCategory();
	List<ExhibitionAccountResponse> getExhibitionAccountByExhibitionCategory(Long exhibitionCategoryId);
}
