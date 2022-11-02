package com.team6.onandthefarm.repository.exhibition;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.exhibition.ExhibitionCategory;

public interface ExhibitionCategoryRepository extends CrudRepository<ExhibitionCategory, Long> {
	@Override
	Optional<ExhibitionCategory> findById(Long exhibitionCategoryId);
}
