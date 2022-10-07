package com.team6.onandthefarm.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFollowingListRequest {
	private Long memberId;
	private String memberRole;
}
