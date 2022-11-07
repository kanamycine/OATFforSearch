package com.team6.onandthefarm.repository.exhibition;

import java.util.List;

import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;

public interface ExhibitionAccountRepository extends CrudRepository<ExhibitionAccount, Long> {
	@Query("select e from ExhibitionAccount e join fetch e.exhibitionCategory where e.exhibitionCategory.exhibitionCategoryId =:exhibitionCategoryId and e.exhibitionAccountStatus = true")
	List<ExhibitionAccount> findByExhibitionCategoryId(@Param("exhibitionCategoryId") Long exhibitionCategoryId);


	@Query("select e from ExhibitionAccount e where e.exhibitionAccountId =:exhibitionAccountId")	// 구좌 Id로 구좌 정보 불러오기
	ExhibitionAccount findByExhibitionAccountId(@Param("exhibitionAccountId") Long exhibitionAccountId);
}
