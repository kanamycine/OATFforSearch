package com.team6.onandthefarm.vo.exhibition;

import com.team6.onandthefarm.vo.PageVo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSelectionResponseResult {
    private List<ModuleSelectionResponse> moduleListResponses;
    private PageVo pageVo;
}
