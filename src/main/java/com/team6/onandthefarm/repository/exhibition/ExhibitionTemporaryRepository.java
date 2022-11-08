package com.team6.onandthefarm.repository.exhibition;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.exhibition.ExhibitionTemporary;

public interface ExhibitionTemporaryRepository extends CrudRepository<ExhibitionTemporary, Long> {
	List<ExhibitionTemporary> findAll(Sort sort);
}
