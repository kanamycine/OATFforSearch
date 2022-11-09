package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.datatool.BadgeDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.ProductDataRequestDto;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductATypeResponses;

public interface DataToolService {
	BannerATypeResponses getBannerATypeItems(BannerDataRequestDto bannerDataRequestDto);
	BadgeATypeResponses getBadgeATypeItems(BadgeDataRequestDto badgeDataRequestDto);
	ProductATypeResponses getProductATypeItems(ProductDataRequestDto productDataRequestDto, Long userId);
}
