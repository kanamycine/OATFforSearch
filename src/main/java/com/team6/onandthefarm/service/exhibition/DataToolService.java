package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.datatool.BadgeDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.ProductDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.SnsDataRequestDto;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductBTypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductCTypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.SnsATypeResponses;

public interface DataToolService {
	BannerATypeResponses getBannerATypeItems(BannerDataRequestDto bannerDataRequestDto);
	BadgeATypeResponses getBadgeATypeItems(BadgeDataRequestDto badgeDataRequestDto);
	ProductATypeResponses getProductATypeItems(ProductDataRequestDto productDataRequestDto, Long userId);
	ProductBTypeResponses getProductBTypeItems(ProductDataRequestDto productDataRequestDto, Long userId);
	ProductCTypeResponses getProductCTypeItems(ProductDataRequestDto productDataRequestDto, Long serId);
	SnsATypeResponses getSnsATypeItems(SnsDataRequestDto snsDataRequestDto);
}
