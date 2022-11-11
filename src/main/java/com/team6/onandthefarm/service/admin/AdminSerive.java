package com.team6.onandthefarm.service.admin;

import com.team6.onandthefarm.dto.admin.AdminReIssueDto;
import com.team6.onandthefarm.vo.admin.AdminLoginResponse;

import javax.servlet.http.HttpServletRequest;

public interface AdminSerive {
    AdminLoginResponse reIssueToken(AdminReIssueDto adminReIssueDto);
    Boolean logout(HttpServletRequest request);
}
