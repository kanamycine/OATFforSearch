package com.team6.onandthefarm.security.oauth.provider;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class NaverOAuth2 implements OAuth2UserUtil {


    private String clientId;

    private String clientSecret;

    private String redirectUrl;

    @Override
    public String getAuthCode() {
        return null;
    }

    @Override
    public String getAccessToken(UserLoginDto userLoginDto) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", userLoginDto.getCode());

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        try {
            // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
            ResponseEntity<String> response = rt.exchange( "https://nid.naver.com/oauth2.0/token", HttpMethod.POST, naverTokenRequest, String.class );
            // JSON -> 액세스 토큰 파싱
            String tokenJson = response.getBody();
            JSONObject rjson = new JSONObject(tokenJson);
            String accessToken = rjson.getString("access_token");

            return accessToken;
        } catch (HttpClientErrorException.BadRequest e){
            log.error("getAccessToken - 잘못된 인가 코드");
            return null;
        }
    }

    @Override
    public OAuth2UserDto getUserInfo(String accessToken) {
        return null;
    }
}
