package com.team6.onandthefarm.security.oauth.provider;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GoogleOAuth2 implements OAuth2UserUtil {


	@Override
	public String getAuthCode() {
		return null;
	}

	@Override
	public String getAccessToken(UserLoginDto userLoginDto) {
		return null;
	}

	@Override
	public OAuth2UserDto getUserInfo(String accessToken) {
		return null;
	}
}
