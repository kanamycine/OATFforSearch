package com.team6.onandthefarm.service.exhibition;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.repository.exhibition.ExhibitionAccountRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionCategoryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemRepository;
import com.team6.onandthefarm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ExhibitionItemServiceImpl implements ExhibitionItemService {

	private ExhibitionAccountRepository exhibitionAccountRepository;
	private ExhibitionCategoryRepository exhibitionCategoryRepository;
	private ExhibitionItemRepository exhibitionItemRepository;
	private DateUtils dateUtils;
	private Environment env;

	public ExhibitionItemServiceImpl(ExhibitionAccountRepository exhibitionAccountRepository,
			ExhibitionCategoryRepository exhibitionCategoryRepository,
			ExhibitionItemRepository exhibitionItemRepository,
			DateUtils dateUtils,
			Environment env){
		this.exhibitionAccountRepository = exhibitionAccountRepository;
		this.exhibitionCategoryRepository = exhibitionCategoryRepository;
		this.exhibitionItemRepository = exhibitionItemRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}
	//
	// @Override
	// public Long createExhibitionProductItem(ExhibitionProductItemFormRequestDto exhibitionProductItemFormRequestDto){
	// 	ModelMapper modelMapper = new ModelMapper();
	// 	modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	//
	// 	ExhibitionProductItem exhibitionProductItem = modelMapper.map(exhibitionProductItemFormRequestDto, ExhibitionProductItem.class);
	//
	// 	Long exhibitionAccountId = exhibitionProductItemFormRequestDto.getExhibitionAccountId();
	// 	Long exhibitionCategoryId = exhibitionProductItemFormRequestDto.getExhibitionCategoryId();
	// 	Optional<ExhibitionAccount> exhibitionAccount = exhibitionAccountRepository.findById(exhibitionAccountId);
	// 	Optional<ExhibitionCategory> exhibitionCategory = exhibitionCategoryRepository.findById(exhibitionCategoryId);
	//
	// 	exhibitionProductItem.setExhibitionAccount(exhibitionAccount.get());
	// 	exhibitionProductItem.setExhibitionCategory(exhibitionCategory.get());
	// 	exhibitionProductItem.setExhibitionProductItemCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
	// 	exhibitionProductItem.setExhibitionProductItemStatus(true);
	//
	// 	return exhibitionProductItemRepository.save(exhibitionProductItem).getExhibitionProductItemId();
	// }
}
