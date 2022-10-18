package com.team6.onandthefarm.vo.sns.profile;

import com.team6.onandthefarm.entity.sns.FeedImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMainFeedResponse {
	private Long feedId;
	private Long feedImageId;
	private String feedImageSrc;
	private String memberName;
}
