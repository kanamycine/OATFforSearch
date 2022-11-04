package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.ModuleFormDto;
import com.team6.onandthefarm.entity.exhibition.Module;
import com.team6.onandthefarm.repository.exhibition.ModuleImgRepository;
import com.team6.onandthefarm.repository.exhibition.ModulePagingRepository;
import com.team6.onandthefarm.repository.exhibition.ModuleRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.util.S3Upload;
import com.team6.onandthefarm.vo.PageVo;
import com.team6.onandthefarm.vo.exhibition.ModuleSelectionResponse;
import com.team6.onandthefarm.vo.exhibition.ModuleSelectionResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ModuleServiceImpl implements ModuleService{
    private final ModuleRepository moduleRepository;

    private final ModulePagingRepository modulePagingRepository;

    private final ModuleImgRepository moduleImgRepository;

    private final DateUtils dateUtils;

    private final S3Upload s3Upload;

    private Environment env;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository,
                             ModulePagingRepository modulePagingRepository,
                             ModuleImgRepository moduleImgRepository,
                             DateUtils dateUtils,
                             Environment env,
                             S3Upload s3Upload){
        this.moduleRepository = moduleRepository;
        this.modulePagingRepository = modulePagingRepository;
        this.moduleImgRepository = moduleImgRepository;
        this.dateUtils = dateUtils;
        this.env = env;
        this.s3Upload = s3Upload;
    }

    @Override
    public Long saveModule(ModuleFormDto moduleFormDto) throws IOException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Module module = modelMapper.map(moduleFormDto, Module.class);

        String url = s3Upload.moduleUpload(moduleFormDto.getImage());
        module.setModuleImgSrc(url);
        module.setModuleCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
        module.setModuleStatus(true);
        module.setModuleUsableStatus(true);

        return moduleRepository.save(module).getModuleId();
    }

    @Override
    public ModuleSelectionResponseResult getAllModuleListOrderByNewest (Integer pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("moduleCreatedAt").descending());

        Page<Module> moduleList = modulePagingRepository.findModuleOrderBy(pageRequest);
        System.out.println(moduleList);
        int totalPage = moduleList.getTotalPages();
        Long totalElements = moduleList.getTotalElements();

        PageVo pageVo = PageVo.builder()
                .totalPage(totalPage)
                .nowPage(pageNumber)
                .totalElement(totalElements)
                .build();

        List<ModuleSelectionResponse> moduleResponseList = new ArrayList<>();

        for(Module m : moduleList){
            ModuleSelectionResponse mResponse = ModuleSelectionResponse.builder()
                    .moduleId(m.getModuleId())
                    .moduleName(m.getModuleName())
                    .moduleContent(m.getModuleContent())
                    .moduleImgSrc(m.getModuleImgSrc())
                    .moduleDataSize(m.getModuleDataSize())
                    .moduleUsableStatus(m.isModuleUsableStatus())
                    .moduleStatus(m.isModuleStatus())
                    .moduleCreatedAt(m.getModuleCreatedAt())
                    .moduleModifiedAt(m.getModuleModifiedAt())
                    .moduleDevelopCompletedAt(m.getModuleDevelopCompletedAt())
                    .moduleDevelopModifiedAt(m.getModuleDevelopModifiedAt())
                    .build();
            moduleResponseList.add(mResponse);
        }

        ModuleSelectionResponseResult moduleListResponse= ModuleSelectionResponseResult.builder()
                .moduleListResponses(moduleResponseList)
                .pageVo(pageVo)
                .build();
        return moduleListResponse;
    }

}
