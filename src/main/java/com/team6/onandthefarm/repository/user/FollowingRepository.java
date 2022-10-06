package com.team6.onandthefarm.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team6.onandthefarm.entity.user.Following;

@Repository
public interface FollowingRepository extends CrudRepository<Following, Long> {
	Following findByFollowingMemberIdAndFollowingMemberRoleAndFollowerMemberIdAndFollowerMemberRole(
			Long followingMemberId, String followingMemberRole, Long followerMemberId, String followerMemberString);

	// @Query("select f from Following f where f.followingMemberId =:followingMemberId and f.followingMemberRole =:followingMemberRole and f.followerMemberId =:followerMemberId and f.followerMemberRole =:followerMemberRole")
	// Following findByAllCondition(Long followingMemberId, String followingMemberRole, Long followerMemberId, String follwerMemberString);
}

