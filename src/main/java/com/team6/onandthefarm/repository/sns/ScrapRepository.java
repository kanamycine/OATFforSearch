package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.Scrap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapRepository extends CrudRepository<Scrap,Long> {
	@Query("select s from Scrap s where s.memberId =:memberId")
	List<Scrap> findScrapListByMemberId(@Param("memberId") Long memberId);

	Scrap findByFeedAndMemberId(Feed feed, Long memberId);
}
