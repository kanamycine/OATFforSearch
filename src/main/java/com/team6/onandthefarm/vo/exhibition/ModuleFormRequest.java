package com.team6.onandthefarm.vo.exhibition;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleFormRequest {
    private String moduleName;

    private String moduleContent;

    private String moduleImgSrc;
}
