package com.team6.onandthefarm.vo.admin;

import com.team6.onandthefarm.security.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse {
    private Token token;

    private String role;
}
