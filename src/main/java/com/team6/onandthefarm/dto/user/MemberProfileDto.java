package com.team6.onandthefarm.dto.user;

import lombok.Data;

@Data
public class MemberProfileDto {
	private Long memberId;
	private Long memberRole;
	private String userName;
	private String memberProfileImage;
}
