package com.team6.onandthefarm.controller.exhibition;

import com.team6.onandthefarm.dto.exhibition.ModuleFormDto;
import com.team6.onandthefarm.service.exhibition.ModuleService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.ModuleFormRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/module")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService){
        this.moduleService = moduleService;
    }

    @PostMapping(value = "/new")
    @ApiOperation(value = "모듈등록")
    public ResponseEntity<BaseResponse<Module>> moduleForm(
            @ApiIgnore Principal principal,
            @RequestPart(value = "image", required = false) MultipartFile photo,
            @RequestPart(value = "data", required = false)ModuleFormRequest moduleFormRequest
    ) throws Exception {
        System.out.println("steadfast");
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ModuleFormDto moduleFormDto = modelMapper.map(moduleFormRequest, ModuleFormDto.class);
        moduleFormDto.setImage(photo);

        Long moduleId = moduleService.saveModule(moduleFormDto);
        System.out.println(photo);
        System.out.println(moduleFormDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("module register completed")
                .data(moduleId)
                .build();

        return new ResponseEntity(baseResponse, HttpStatus.CREATED);

    }


}