package com.team6.onandthefarm.service.exhibition;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.exhibition.datatool.BannerATypeRequestDto;
import com.team6.onandthefarm.entity.exhibition.item.Banner;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItems;
import com.team6.onandthefarm.repository.exhibition.DataPickerRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionAccountRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionCategoryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemsRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionRepository;
import com.team6.onandthefarm.repository.exhibition.item.BannerRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponse;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponses;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DataToolServiceImpl implements DataToolService{
	private ExhibitionAccountRepository exhibitionAccountRepository;
	private ExhibitionCategoryRepository exhibitionCategoryRepository;
	private ExhibitionItemsRepository exhibitionItemsRepository;
	private ExhibitionItemRepository exhibitionItemRepository;
	private ExhibitionRepository exhibitionRepository;
	private DataPickerRepository dataPickerRepository;
	private BannerRepository bannerRepository;
	private DateUtils dateUtils;
	private Environment env;

	public DataToolServiceImpl(
			ExhibitionAccountRepository exhibitionAccountRepository,
			ExhibitionCategoryRepository exhibitionCategoryRepository,
			ExhibitionItemsRepository exhibitionItemsRepository,
			ExhibitionItemRepository exhibitionItemRepository,
			ExhibitionRepository exhibitionRepository,
			DataPickerRepository dataPickerRepository,
			BannerRepository bannerRepository, DateUtils dateUtils, Environment env) {
		this.exhibitionAccountRepository = exhibitionAccountRepository;
		this.exhibitionCategoryRepository = exhibitionCategoryRepository;
		this.exhibitionItemsRepository = exhibitionItemsRepository;
		this.exhibitionItemRepository = exhibitionItemRepository;
		this.exhibitionRepository = exhibitionRepository;
		this.dataPickerRepository = dataPickerRepository;
		this.bannerRepository = bannerRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	@Override
	public BannerATypeResponses getBannerATypeItems(BannerATypeRequestDto bannerATypeRequestDto){
		ExhibitionItems exhibitionItems = exhibitionItemsRepository.findById(bannerATypeRequestDto.getItemsId()).get();
		BannerATypeResponses bannerATypeResponsesResult = new BannerATypeResponses();
		List<BannerATypeResponse> BannerATypeResponses = new ArrayList<>();

		List<ExhibitionItem> items = exhibitionItemRepository.findExhibitionItemByExhibitionItemsId(exhibitionItems.getExhibitionItemsId());
		for (ExhibitionItem item : items) {
			Banner banner = bannerRepository.findById(item.getExhibitionItemNumber()).get();
			BannerATypeResponse bannerATypeResponse = BannerATypeResponse.builder()
					.ImgSrc(banner.getBannerImg())
					.connectUrl(banner.getBannerConnectUrl())
					.build();
			BannerATypeResponses.add(bannerATypeResponse);
		}
		bannerATypeResponsesResult.setBannerATypeResponses(BannerATypeResponses);

		return bannerATypeResponsesResult;
	}
}
