package org.winey.server.service.auth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSignInService {
    @Value("${jwt.KAKAO_URL}")
    private String KAKAO_URL;

    public String getKaKaoId(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ accessToken);
        HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseData;
        log.info("before kakao post");
        responseData = restTemplate.postForEntity(KAKAO_URL,httpEntity,Object.class);
        log.info("after kakao post");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(); //소셜 id만 가져오는듯.
    }
}
