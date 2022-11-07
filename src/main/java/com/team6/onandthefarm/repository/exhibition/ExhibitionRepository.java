package com.team6.onandthefarm.repository.exhibition;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.exhibition.Exhibition;

public interface ExhibitionRepository extends CrudRepository<Exhibition, Long> {

	@Query("select e from Exhibition e where e.exhibitionStatus =:true")
	List<Exhibition> getTrueExhibitions();
}
