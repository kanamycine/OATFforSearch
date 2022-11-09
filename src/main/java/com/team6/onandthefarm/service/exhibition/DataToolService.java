package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.datatool.BadgeATypeRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerATypeRequestDto;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponses;

public interface DataToolService {
	BannerATypeResponses getBannerATypeItems(BannerATypeRequestDto bannerATypeRequestDto);
	BadgeATypeResponses getBadgeATypeItems(BadgeATypeRequestDto badgeATypeRequestDto);
}
