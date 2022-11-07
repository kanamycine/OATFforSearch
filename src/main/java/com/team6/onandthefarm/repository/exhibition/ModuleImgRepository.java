package com.team6.onandthefarm.repository.exhibition;

import com.team6.onandthefarm.entity.exhibition.Module;
import com.team6.onandthefarm.entity.exhibition.ModuleImg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleImgRepository extends CrudRepository<ModuleImg, Long> {

    List<ModuleImg> findByModule(Module module);
}
