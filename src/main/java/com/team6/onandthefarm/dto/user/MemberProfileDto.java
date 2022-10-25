package com.team6.onandthefarm.dto.user;

import lombok.Data;

@Data
public class MemberProfileDto {

	private Long memberId;

	private String memberRole;

	private String userName;

	private String memberProfileImage;

	private Boolean loginMemberStatus;

	private Long loginMemberId;

	private String loginMemberRole;
}
