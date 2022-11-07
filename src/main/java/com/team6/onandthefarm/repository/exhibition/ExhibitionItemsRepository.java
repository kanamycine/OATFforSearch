package com.team6.onandthefarm.repository.exhibition;

import com.team6.onandthefarm.entity.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItems;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExhibitionItemsRepository extends CrudRepository<ExhibitionItems, Long> {

    // 구좌 디테일 데이터 수 불러오기
    @Query("select p from ExhibitionItems p where p.exhibitionAccount.exhibitionAccountId =:exhibitionAccountId")
    List<ExhibitionItems> findExhibitionItemsDetail(@Param("exhibitionAccountId") Long exhibitionAccountId);

    @Query("select p from ExhibitionItems p where p.exhibitionAccount.exhibitionAccountId =:exhibitionAccountId")
    List<ExhibitionItems> findExhibitionItemsByExhibitionAccountId(@Param("exhibitionAccountId") Long exhibitionAccountId);

}
