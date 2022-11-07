package com.team6.onandthefarm.service.exhibition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.team6.onandthefarm.dto.exhibition.ExhibitionItemsFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryApplyFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryDeleteFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionTemporaryUpdateFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.Exhibition;
import com.team6.onandthefarm.entity.exhibition.ExhibitionTemporary;
import com.team6.onandthefarm.repository.exhibition.ExhibitionTemporaryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionRepository;
import com.team6.onandthefarm.vo.exhibition.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.exhibition.DataPickerFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionItemFormRequestDto;
import com.team6.onandthefarm.entity.exhibition.DataPicker;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItems;
import com.team6.onandthefarm.repository.exhibition.DataPickerRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionCategoryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionAccountRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemsRepository;
import com.team6.onandthefarm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ExhibitionServiceImpl implements ExhibitionService{
	private ExhibitionAccountRepository exhibitionAccountRepository;
	private ExhibitionCategoryRepository exhibitionCategoryRepository;
	private ExhibitionItemsRepository exhibitionItemsRepository;
	private ExhibitionItemRepository exhibitionItemRepository;
	private ExhibitionTemporaryRepository exhibitionTemporaryRepository;
	private ExhibitionRepository exhibitionRepository;
	private DataPickerRepository dataPickerRepository;
	private DateUtils dateUtils;
	private Environment env;

	public ExhibitionServiceImpl(ExhibitionAccountRepository exhibitionAccountRepository,
								ExhibitionCategoryRepository exhibitionCategoryRepository,
								ExhibitionItemsRepository exhibitionItemsRepository,
								ExhibitionItemRepository exhibitionItemRepository,
								DataPickerRepository dataPickerRepository,
								ExhibitionTemporaryRepository exhibitionTemporaryRepository,
								ExhibitionRepository exhibitionRepository,
								DateUtils dateUtils,
								Environment env){
		this.exhibitionAccountRepository = exhibitionAccountRepository;
		this.exhibitionCategoryRepository = exhibitionCategoryRepository;
		this.exhibitionItemsRepository = exhibitionItemsRepository;
		this.exhibitionItemRepository = exhibitionItemRepository;
		this.dataPickerRepository = dataPickerRepository;
		this.exhibitionTemporaryRepository = exhibitionTemporaryRepository;
		this.exhibitionRepository = exhibitionRepository;
		this.env = env;
		this.dateUtils = dateUtils;
	}


	@Override
	public Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccount exhibitionAccount = modelMapper.map(exhibitionAccountFormDto, ExhibitionAccount.class);

		Long categoryId = exhibitionAccountFormDto.getExhibitionAccountCategoryId();
		Optional<ExhibitionCategory> exhibitionCategory = exhibitionCategoryRepository.findById(categoryId);

		exhibitionAccount.setExhibitionCategory(exhibitionCategory.get());
		exhibitionAccount.setExhibitionAccountCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
		exhibitionAccount.setExhibitionAccountStatus(true);

		Long exhibitionAccountId = exhibitionAccountRepository.save(exhibitionAccount).getExhibitionAccountId();


		List<ExhibitionItemsFormRequestDto> exhibitionItemsFormRequestDtos = exhibitionAccountFormDto.getExhibitionItemsFormRequestDtos();
		for (ExhibitionItemsFormRequestDto exhibitionItemsFormRequestDto : exhibitionItemsFormRequestDtos){
			ExhibitionItems exhibitionItems = ExhibitionItems.builder()
					.exhibitionAccount(exhibitionAccountRepository.findById(exhibitionAccountId).get())
					.exhibitionItemsName(exhibitionItemsFormRequestDto.getExhibitionItemsName())
					.exhibitionItemsDetail(exhibitionItemsFormRequestDto.getExhibitionItemsDetail())
					.build();
			Long exhibitionItemsId = exhibitionItemsRepository.save(exhibitionItems).getExhibitionItemsId();
			List<ExhibitionItemFormRequestDto> exhibitionItemFormRequestDtos = exhibitionItemsFormRequestDto.getExhibitionItemFormRequestDtos();

			for(ExhibitionItemFormRequestDto itemFormRequestDto : exhibitionItemFormRequestDtos) {
				ExhibitionItem exhibitionItem = modelMapper.map(itemFormRequestDto, ExhibitionItem.class);

				exhibitionItem.setExhibitionItemCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
				exhibitionItem.setExhibitionItemStatus(true);
				exhibitionItem.setExhibitionItems(exhibitionItemsRepository.findById(exhibitionItemsId).get());

				exhibitionItemRepository.save(exhibitionItem);
			}

		}
		return exhibitionAccountId;
	}
	@Override
	public Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto){

		Optional<ExhibitionAccount> exhibitionAccount = exhibitionAccountRepository.findById(exhibitionAccountUpdateFormDto.getExhibitionAccountId());
		Optional<ExhibitionCategory> exhibitionCategory = exhibitionCategoryRepository.findById(exhibitionAccountUpdateFormDto.getExhibitionCategoryId());

		exhibitionAccount.get().setExhibitionAccountName(exhibitionAccountUpdateFormDto.getExhibitionAccountName());
		exhibitionAccount.get().setExhibitionCategory(exhibitionCategory.get());
		exhibitionAccount.get().setExhibitionAccountStartTime(exhibitionAccountUpdateFormDto.getExhibitionAccountStartTime());
		exhibitionAccount.get().setExhibitionAccountEndTime(exhibitionAccountUpdateFormDto.getExhibitionAccountEndTime());
		exhibitionAccount.get().setExhibitionAccountStatus(exhibitionAccountUpdateFormDto.isExhibitionAccountStatus());

		return exhibitionAccount.get().getExhibitionAccountId();
	}

	@Override
	public Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto){

		Optional<ExhibitionAccount> exhibitionAccount = exhibitionAccountRepository.findById(exhibitionAccountDeleteDto.getExhibitionAccountId());
		exhibitionAccount.get().setExhibitionAccountStatus(false);
		exhibitionAccount.get().setExhibitionAccountModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		return exhibitionAccount.get().getExhibitionAccountId();
	}

	@Override
	public List<ExhibitionAccountResponse> getExhibitionAccountByExhibitionCategory(Long exhibitionCategoryId){

		List<ExhibitionAccountResponse> responses = new ArrayList<>();
		List<ExhibitionAccount> exhibitionAccounts = exhibitionAccountRepository.findByExhibitionCategoryId(exhibitionCategoryId);
		for (ExhibitionAccount e : exhibitionAccounts) {
			ExhibitionAccountResponse exhibitionAccountResponse = new ExhibitionAccountResponse();
			exhibitionAccountResponse.setExhibitionAccountId(e.getExhibitionAccountId());
			exhibitionAccountResponse.setExhibitionAccountName(e.getExhibitionAccountName());
			responses.add(exhibitionAccountResponse);
		}
		return responses;
	}

	@Override
	public List<ExhibitionCategoryResponse> getAllExhibitionCategory(){
		List<ExhibitionCategoryResponse> responses = new ArrayList<>();
		List<ExhibitionCategory> exhibitionCategories = new ArrayList<>();
		Iterable<ExhibitionCategory> all = exhibitionCategoryRepository.findAll();
		for (ExhibitionCategory exhibitionCategory : all) {
			exhibitionCategories.add(exhibitionCategory);
		}

		for (ExhibitionCategory e : exhibitionCategories) {
			ExhibitionCategoryResponse  exhibitionCategoryResponse = new ExhibitionCategoryResponse();
			exhibitionCategoryResponse.setExhibitionCategoryId(e.getExhibitionCategoryId());
			exhibitionCategoryResponse.setExhibitionCategoryName(e.getExhibitionCategoryName());
			responses.add(exhibitionCategoryResponse);
		}
		return responses;
	}

	@Override
	public List<ExhibitionItemsInfoResponse> getExhibitionItemsInfos(Long exhibitionAccountId){
		List<ExhibitionItems> exhibitionItemss = exhibitionItemsRepository.findExhibitionItemsByExhibitionAccountId(exhibitionAccountId);
		List<ExhibitionItemsInfoResponse> result = new ArrayList<>();
		for (ExhibitionItems exhibitionItems : exhibitionItemss) {
			ExhibitionItemsInfoResponse exhibitionItemsInfoResponse = ExhibitionItemsInfoResponse.builder()
					.exhibitionItemsId(exhibitionItems.getExhibitionItemsId())
					.exhibitionItemsName(exhibitionItems.getExhibitionItemsName())
					.exhibitionItemsDetail(exhibitionItems.getExhibitionItemsDetail())
					.build();
			result.add(exhibitionItemsInfoResponse);
		}
		return result;
	}

	@Override
	public List<ExhibitionItemInfoResponse> getExhibitionItemInfos(Long exhibitionItemsId){
		List<ExhibitionItem> exhibitionItems = exhibitionItemRepository.findExhibitionItemByExhibitionItemsId(exhibitionItemsId);
		List<ExhibitionItemInfoResponse> result = new ArrayList<>();
		for (ExhibitionItem exhibitionItem : exhibitionItems) {
			ExhibitionItemInfoResponse exhibitionItemInfoResponse = ExhibitionItemInfoResponse.builder()
					.exhibitionItemId(exhibitionItem.getExhibitionItemId())
					.exhibitionItemNumber(exhibitionItem.getExhibitionItemNumber())
					.exhibitionItemPriority(exhibitionItem.getExhibitionItemPriority())
					.build();
			result.add(exhibitionItemInfoResponse);
		}
		return result;
	}

	@Override
	public Long createDataPicker(DataPickerFormRequestDto dataPickerFormRequestDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		DataPicker dataPicker = modelMapper.map(dataPickerFormRequestDto, DataPicker.class);

		dataPicker.setExhibitionCategory(exhibitionCategoryRepository.findById(dataPickerFormRequestDto.getExhibitionCategoryId()).get());
		dataPicker.setDataPickerCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
		dataPicker.setDataPickerStatus(true);

		return dataPickerRepository.save(dataPicker).getDataPickerId();
	}

	@Override
	public ExhibitionAccountDetailResponse getExhibitionAccountDetail(Long exhibitionAccountId){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccountDetailResponse exhibitionAccountDetail = new ExhibitionAccountDetailResponse();	// 프론트에 보내 줄 responses 빈 class 생성
		exhibitionAccountDetail.setExhibitionAccountItemsDetailResponseList(new ArrayList<>());

		ExhibitionAccount exhibitionAccount = exhibitionAccountRepository.findByExhibitionAccountId(exhibitionAccountId);
		exhibitionAccountDetail.setExhibitionAccountId(exhibitionAccount.getExhibitionAccountId());
		exhibitionAccountDetail.setExhibitionAccountName(exhibitionAccount.getExhibitionAccountName());
		exhibitionAccountDetail.setExhibitionAccountDetail(exhibitionAccount.getExhibitionAccountDetail());
		exhibitionAccountDetail.setExhibitionAccountStartTime(exhibitionAccount.getExhibitionAccountStartTime());
		exhibitionAccountDetail.setExhibitionAccountEndTime(exhibitionAccount.getExhibitionAccountEndTime());

		List<ExhibitionItems> exhibitionsItemsDetailList =  exhibitionItemsRepository.findExhibitionItemsDetail(exhibitionAccountId);
		for(ExhibitionItems items : exhibitionsItemsDetailList) {
			ExhibitionAccountItemsDetailResponse exhibitionAccountItemsDetail = new ExhibitionAccountItemsDetailResponse();	// 소재 리스트 respose 선언
			exhibitionAccountItemsDetail.setExhibitionAccountItemDetailResponseList(new ArrayList<>());

			exhibitionAccountItemsDetail.setExhibitionItemsId(items.getExhibitionItemsId());
			exhibitionAccountItemsDetail.setExhibitionItemsName(items.getExhibitionItemsName());
			exhibitionAccountItemsDetail.setExhibitionItemsDetail(items.getExhibitionItemsDetail());

			List<ExhibitionItem> exhibitionItemDetailList = exhibitionItemRepository.findExhibitionItemDetail(items.getExhibitionItemsId());
			for(ExhibitionItem item  : exhibitionItemDetailList){
				ExhibitionAccountItemDetailResponse exhibitionAccountItemDetail = new ExhibitionAccountItemDetailResponse();	// 소재 respose 선언
				exhibitionAccountItemDetail.setExhibitionItemId(item.getExhibitionItemId());
				exhibitionAccountItemDetail.setExhibitionItemCategoryId(item.getExhibitionItemCategoryId());
				exhibitionAccountItemDetail.setExhibitionItemPriority(item.getExhibitionItemPriority());

				exhibitionAccountItemsDetail.getExhibitionAccountItemDetailResponseList().add(exhibitionAccountItemDetail);	// 소재 리스트에 소재 추가
			}
			exhibitionAccountDetail.getExhibitionAccountItemsDetailResponseList().add(exhibitionAccountItemsDetail);	// 구좌에 소재 리스트 추가
		}
		return exhibitionAccountDetail;
	}

	@Override
	public Long createExhibitionTemporary(ExhibitionTemporaryFormRequestDto exhibitionTemporaryFormRequestDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionTemporary exhibitionTemporary = modelMapper.map(exhibitionTemporaryFormRequestDto, ExhibitionTemporary.class);

		exhibitionTemporary.setExhibitionTemporaryCategory(exhibitionCategoryRepository.findById(exhibitionTemporaryFormRequestDto.getExhibitionTemporaryCategoryId()).get());
		exhibitionTemporary.setExhibitionActivation(true);
		Long exhibitionTemporaryId = exhibitionTemporaryRepository.save(exhibitionTemporary).getExhibitionTemporaryId();

		return exhibitionTemporaryId;
	}

	@Override
	public Long updateExhibitionTemporary(ExhibitionTemporaryUpdateFormRequestDto exhibitionTemporaryUpdateFormRequestDto){

		ExhibitionTemporary savedExhibitionTemporary = exhibitionTemporaryRepository.findById(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryId()).get();

		savedExhibitionTemporary.setExhibitionTemporaryAccountId(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryAccountId());
		savedExhibitionTemporary.setExhibitionTemporaryCategory(exhibitionCategoryRepository.findById(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryCategoryId()).get());
		savedExhibitionTemporary.setExhibitionTemporaryItemsId(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryItemsId());
		savedExhibitionTemporary.setExhibitionTemporaryModuleName(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryModuleName());
		savedExhibitionTemporary.setExhibitionTemporaryDataPicker(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryDataPicker());
		savedExhibitionTemporary.setExhibitionActivation(exhibitionTemporaryUpdateFormRequestDto.getExhibitionActivation());
		savedExhibitionTemporary.setExhibitionTemporaryPriority(exhibitionTemporaryUpdateFormRequestDto.getExhibitionTemporaryPriority());

		ExhibitionTemporary exhibitionTemporaryId = exhibitionTemporaryRepository.save(savedExhibitionTemporary);
		return exhibitionTemporaryId.getExhibitionTemporaryId();
	}

	@Override
	public Long deleteExhibitionTemporary(ExhibitionTemporaryDeleteFormRequestDto exhibitionTemporaryDeleteFormRequestDto){
		Long exhibitionTemporaryId = exhibitionTemporaryDeleteFormRequestDto.getExhibitionTemporaryId();
		ExhibitionTemporary targetExhibitionTemporary = exhibitionTemporaryRepository.findById(exhibitionTemporaryId).get();
		exhibitionTemporaryRepository.delete(targetExhibitionTemporary);

		return exhibitionTemporaryId;
	}

	@Override
	public List<Long> applyExhibitionTemporary(ExhibitionTemporaryApplyFormRequestDto exhibitionTemporaryApplyFormRequestDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		List<Long> exhibitionIds = new ArrayList<>();

		List<Long> exhibitionTemporaryIds = exhibitionTemporaryApplyFormRequestDto.getExhibitionTemporaryIds();

		exhibitionRepository.deleteAll();

		for (Long exhibitionTemporaryId : exhibitionTemporaryIds) {
			ExhibitionTemporary exhibitionTemporary = exhibitionTemporaryRepository.findById(exhibitionTemporaryId).get();
			Exhibition exhibition = modelMapper.map(exhibitionTemporary, Exhibition.class);
			exhibitionIds.add(exhibitionRepository.save(exhibition).getExhibitionId());
		}

		return exhibitionIds;

	}
}
