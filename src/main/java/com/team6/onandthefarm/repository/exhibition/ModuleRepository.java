package com.team6.onandthefarm.repository.exhibition;

import com.team6.onandthefarm.entity.exhibition.Module;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
}
