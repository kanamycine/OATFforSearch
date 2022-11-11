package com.team6.onandthefarm.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class AdminReIssueDto {
    private String accessToken;
    private String refreshToken;
}
