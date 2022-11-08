package com.team6.onandthefarm.service.exhibition;

import java.util.List;

import com.team6.onandthefarm.dto.exhibition.DataPickerFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountPriorityUpdateFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemPriorityUpdateFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryApplyFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryDeleteFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryPriorityUpdateFormsRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryUpdateFormRequestDto;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountDetailResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionAccountResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionCategoryResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionItemInfoResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionItemsInfoResponse;
import com.team6.onandthefarm.vo.exhibition.ExhibitionSelectionResponseResult;
import com.team6.onandthefarm.vo.exhibition.ExhibitionTemporaryAllResponse;

public interface ExhibitionService {
	Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto);
	Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto);
	Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto);
	List<ExhibitionCategoryResponse> getAllExhibitionCategory();
	List<ExhibitionAccountResponse> getExhibitionAccountByExhibitionCategory(Long exhibitionCategoryId);
	Long createDataPicker(DataPickerFormRequestDto dataPickerFormRequestDto);
	ExhibitionSelectionResponseResult getAllExhibitionListOrderByNewest(Integer pageNumber);
	ExhibitionAccountDetailResponse getExhibitionAccountDetail(Long exhibitionAccountId);
	Long createExhibitionTemporary(ExhibitionTemporaryFormRequestDto  exhibitionTemporaryFormRequestDto);
	Long updateExhibitionTemporary(ExhibitionTemporaryUpdateFormRequestDto exhibitionTemporaryUpdateFormRequestDto);
	Long deleteExhibitionTemporary(ExhibitionTemporaryDeleteFormRequestDto exhibitionTemporaryDeleteFormRequestDto);
	List<Long> applyExhibitionTemporary(ExhibitionTemporaryApplyFormRequestDto exhibitionTemporaryApplyFormRequestDto);
	List<ExhibitionItemsInfoResponse> getExhibitionItemsInfos(Long exhibitionAccountId);
	List<ExhibitionItemInfoResponse> getExhibitionItemInfos(Long exhibitionItemsId);
	List<Long> updateExhibitionItemPriority(ExhibitionItemPriorityUpdateFormsRequestDto exhibitionItemPriorityUpdateFormsRequestDto);
	List<Long> updateExhibitionAccountPriority(ExhibitionAccountPriorityUpdateFormsRequestDto exhibitionAccountPriorityUpdateFormsRequestDto);
	List<Long> updateExhibitionTemporaryPriority(ExhibitionTemporaryPriorityUpdateFormsRequestDto exhibitionTemporaryPriorityUpdateFormsRequestDto);
	List<ExhibitionTemporaryAllResponse> getAllExhibitionTemporary();
}
