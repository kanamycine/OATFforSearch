package com.team6.onandthefarm.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Following {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long followingId;

	private Long followingMemberId;

	private String followingMemberRole;

	private Long followerMemberId;

	private String followerMemberRole;
}
