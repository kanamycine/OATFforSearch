package com.team6.onandthefarm.service.exhibition;

import java.io.IOException;

import com.team6.onandthefarm.dto.exhibition.item.BadgeFormRequestDto;
import com.team6.onandthefarm.dto.exhibition.item.BannerFormRequestDto;

public interface ExhibitionItemService {
	Long createBanner(BannerFormRequestDto bannerFormRequestDto) throws IOException;
	Long createBadge(BadgeFormRequestDto badgeFormRequestDto)throws IOException;
}
