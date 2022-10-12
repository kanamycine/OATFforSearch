package com.team6.onandthefarm.vo.sns.profile;

import com.team6.onandthefarm.entity.sns.FeedImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainScrapResponse {
	private Long feedId;
	private FeedImage feedImg;
	private String memberName;
}
