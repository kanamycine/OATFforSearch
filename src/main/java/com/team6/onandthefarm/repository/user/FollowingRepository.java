package com.team6.onandthefarm.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team6.onandthefarm.entity.user.Following;

@Repository
public interface FollowingRepository extends CrudRepository<Following, Long> {
	Optional<Following> findByFollowingMemberIdAndFollowerMemberId(
			Long followingMemberId, Long followerMemberId);
}

