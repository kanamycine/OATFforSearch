package com.team6.onandthefarm.vo.user;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQnaRequest {
    private Long productId;

    private Long userId;

    private String productQnaContent;

}
