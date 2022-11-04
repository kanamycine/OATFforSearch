package com.team6.onandthefarm.service.exhibition;

import com.team6.onandthefarm.dto.exhibition.ModuleFormDto;
import com.team6.onandthefarm.entity.exhibition.Module;
import com.team6.onandthefarm.repository.exhibition.ModuleImgRepository;
import com.team6.onandthefarm.repository.exhibition.ModuleRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.util.S3Upload;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@Slf4j
public class ModuleServiceImpl implements ModuleService{
    private final ModuleRepository moduleRepository;

    private final ModuleImgRepository moduleImgRepository;

    private final DateUtils dateUtils;

    private final S3Upload s3Upload;

    private Environment env;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository,
                             ModuleImgRepository moduleImgRepository,
                             DateUtils dateUtils,
                             Environment env,
                             S3Upload s3Upload){
        this.moduleRepository = moduleRepository;
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

}
