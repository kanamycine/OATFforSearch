package com.team6.onandthefarm.repository.exhibition;

import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItems;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExhibitionItemRepository extends CrudRepository<ExhibitionItem, Long> {    // 소재 데이터 수 불러오기
    @Query("select p from ExhibitionItem p where p.exhibitionItems.exhibitionItemsId =:exhibitionItemsId")
    List<ExhibitionItem> findExhibitionItemDetail(@Param("exhibitionItemsId") Long exhibitionItemsId);

}
