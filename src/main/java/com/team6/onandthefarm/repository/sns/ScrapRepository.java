package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.Scrap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends CrudRepository<Scrap,Long> {
	@Query("select s from Scrap s where s.memberId =:memberId")
	List<Scrap> findScrapListByMemberId(@Param("memberId") Long memberId);

	@Query("select s from Scrap s where s.memberId =:memberId")
	Page<Scrap> findMainScrapListByMemberId(PageRequest pageRequest, @Param("memberId") Long memberId);

	Optional<Scrap> findByFeedAndMemberId(Feed feed, Long memberId);
}
