package com.team6.onandthefarm.repository.exhibition;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.exhibition.ExhibitionAccount;

public interface ExhibitionAccountRepository extends CrudRepository<ExhibitionAccount, Long> {
	@Query("select e from ExhibitionAccount e join fetch e.exhibitionCategory where e.exhibitionCategory.exhibitionCategoryId =:exhibitionCategoryId and e.exhibitionAccountStatus =:true")
	List<ExhibitionAccount> findByExhibitionCategoryId(@Param("exhibitionCategoryId") Long exhibitionCategoryId);
}
