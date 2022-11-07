package com.team6.onandthefarm.vo.exhibition;

import com.team6.onandthefarm.entity.exhibition.Module;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSelectionResponse {
    private Long moduleId;

    private String moduleName;

    private String moduleContent;

    private String moduleImgSrc;

    private Integer moduleDataSize;

    private boolean moduleUsableStatus;

    private boolean moduleStatus;

    private String moduleCreatedAt;

    private String moduleModifiedAt;

    private String moduleDevelopCompletedAt;

    private String moduleDevelopModifiedAt;

    public ModuleSelectionResponse(Module module){
        this.moduleId = module.getModuleId();
        this.moduleId = module.getModuleId();

        this.moduleName = module.getModuleName();

        this.moduleContent = module.getModuleContent();

        this.moduleImgSrc = module.getModuleImgSrc();

        this.moduleDataSize = module.getModuleDataSize();

        this.moduleUsableStatus = true;

        this.moduleStatus = true;

        this.moduleCreatedAt = getModuleCreatedAt();

        this.moduleModifiedAt = getModuleModifiedAt();

        this.moduleDevelopCompletedAt = getModuleDevelopCompletedAt();

        this.moduleDevelopModifiedAt = getModuleDevelopModifiedAt();
    }
}
