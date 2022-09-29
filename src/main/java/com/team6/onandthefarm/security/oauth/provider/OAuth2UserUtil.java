package com.team6.onandthefarm.security.oauth.provider;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import org.springframework.stereotype.Component;

@Component
public interface OAuth2UserUtil {

	String getAuthCode();
	String getAccessToken(UserLoginDto userLoginDto);

	OAuth2UserDto getUserInfo(String accessToken);

}
