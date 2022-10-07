package com.team6.onandthefarm.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFollowingRequest {
	private Long followerMemberId;
	private String followerMemberRole;
}
