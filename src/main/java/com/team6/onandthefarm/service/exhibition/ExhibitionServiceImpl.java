package com.team6.onandthefarm.service.exhibition;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountDeleteDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountFormDto;
import com.team6.onandthefarm.dto.exhibition.ExhibitionAccountUpdateFormDto;
import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;
import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;
import com.team6.onandthefarm.repository.exhibition.ExhibitionCategoryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionRepository;
import com.team6.onandthefarm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ExhibitionServiceImpl implements ExhibitionService{
	private ExhibitionRepository exhibitionRepository;
	private ExhibitionCategoryRepository exhibitionCategoryRepository;
	private DateUtils dateUtils;
	private Environment env;

	public ExhibitionServiceImpl(ExhibitionRepository exhibitionRepository,
								ExhibitionCategoryRepository exhibitionCategoryRepository,
								DateUtils dateUtils,
								Environment env){
		this.exhibitionRepository = exhibitionRepository;
		this.exhibitionCategoryRepository = exhibitionCategoryRepository;
		this.env = env;
		this.dateUtils = dateUtils;
	}


	@Override
	public Long createExhibitionAccount(ExhibitionAccountFormDto exhibitionAccountFormDto) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ExhibitionAccount exhibitionAccount = modelMapper.map(exhibitionAccountFormDto, ExhibitionAccount.class);

		Long categoryId = exhibitionAccountFormDto.getExhibitionCategoryId();
		Optional<ExhibitionCategory> exhibitionCategory = exhibitionCategoryRepository.findById(categoryId);

		exhibitionAccount.setExhibitionCategory(exhibitionCategory.get());
		exhibitionAccount.setExhibitionAccountCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
		exhibitionAccount.setExhibitionAccountStatus(true);
		exhibitionAccount.setExhibitionAccountUsableStatus(false);

		return exhibitionRepository.save(exhibitionAccount).getExhibitionAccountId();
	}
	@Override
	public Long updateExhibitionAccount(ExhibitionAccountUpdateFormDto exhibitionAccountUpdateFormDto){

		Optional<ExhibitionAccount> exhibitionAccount = exhibitionRepository.findById(exhibitionAccountUpdateFormDto.getExhibitionAccountId());
		Optional<ExhibitionCategory> exhibitionCategory = exhibitionCategoryRepository.findById(exhibitionAccountUpdateFormDto.getExhibitionCategoryId());

		exhibitionAccount.get().setExhibitionAccountName(exhibitionAccountUpdateFormDto.getExhibitionAccountName());
		exhibitionAccount.get().setExhibitionCategory(exhibitionCategory.get());
		exhibitionAccount.get().setExhibitionAccountTime(exhibitionAccountUpdateFormDto.getExhibitionAccountTime());
		exhibitionAccount.get().setExhibitionAccountUsableStatus(exhibitionAccountUpdateFormDto.isExhibitionAccountUsableStatus());
		exhibitionAccount.get().setExhibitionAccountStatus(exhibitionAccountUpdateFormDto.isExhibitionAccountStatus());

		return exhibitionAccount.get().getExhibitionAccountId();
	}

	@Override
	public Long deleteExhibitionAccount(ExhibitionAccountDeleteDto exhibitionAccountDeleteDto){

		Optional<ExhibitionAccount> exhibitionAccount = exhibitionRepository.findById(exhibitionAccountDeleteDto.getExhibitionAccountId());
		exhibitionAccount.get().setExhibitionAccountStatus(false);
		exhibitionAccount.get().setExhibitionAccountModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		return exhibitionAccount.get().getExhibitionAccountId();
	}
}
