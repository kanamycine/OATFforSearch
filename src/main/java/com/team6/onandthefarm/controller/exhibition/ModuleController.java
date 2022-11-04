package com.team6.onandthefarm.controller.exhibition;

import com.team6.onandthefarm.dto.exhibition.ModuleFormDto;
import com.team6.onandthefarm.service.exhibition.ModuleService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.exhibition.ModuleFormRequest;
import com.team6.onandthefarm.vo.exhibition.ModuleSelectionResponse;
import com.team6.onandthefarm.vo.exhibition.ModuleSelectionResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value="/list/all/newest/{page-no}")
    @ApiOperation(value="모든 모듈 최신순 조회")
    public ResponseEntity<BaseResponse<ModuleSelectionResponseResult>> getAllModuleList(
            @PathVariable("page-no") String pageNumber){
        System.out.println("dksajtiodasj;lktajsd;fkl");
        System.out.println(Integer.valueOf(pageNumber));
        ModuleSelectionResponseResult modules = moduleService.getAllModuleListOrderByNewest(Integer.valueOf(pageNumber));

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("getting All module by newest")
                .data(modules)
                .build();

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

}