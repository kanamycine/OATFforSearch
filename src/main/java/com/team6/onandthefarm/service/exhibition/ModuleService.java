package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.ModuleFormDto;
import com.team6.onandthefarm.vo.exhibition.ModuleSelectionResponseResult;

import java.io.IOException;

public interface ModuleService {
    Long saveModule(ModuleFormDto moduleFormDto) throws IOException;

    ModuleSelectionResponseResult getAllModuleListOrderByNewest(Integer pageNumber);

}
