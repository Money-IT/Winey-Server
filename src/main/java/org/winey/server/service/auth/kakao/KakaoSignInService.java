package org.winey.server.service.auth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.winey.server.exception.Error;
import org.winey.server.exception.model.UnprocessableEntityException;

import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class KakaoSignInService {
	@Value("${jwt.KAKAO_URL}")
	private String KAKAO_URL;

	@Value("${jwt.KAKAO_AK}")
	private String KAKAO_AK;

	@Value("${jwt.KAKAO_WITHDRAW_URL}")
	private String KAKAO_WITHDRAW;

	public String getKaKaoId(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<Object> responseData;
		responseData = restTemplate.postForEntity(KAKAO_URL, httpEntity, Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.convertValue(responseData.getBody(), Map.class).get("id").toString(); //소셜 id만 가져오는듯.
	}

	public String withdrawKakao(String socialId) {
		ResponseEntity<Object> responseData = requestKakaoServer(socialId);
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap profileResponse = (HashMap)objectMapper.convertValue(responseData.getBody(), Map.class);
		return profileResponse.get("id").toString();
	}

	private ResponseEntity<Object> requestKakaoServer(String socialId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.add("Authorization", "KakaoAK " + KAKAO_AK);

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.set("target_id_type", "user_id");
		param.set("target_id", socialId);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
		try {
			return restTemplate.postForEntity(KAKAO_WITHDRAW, httpEntity, Object.class);
		} catch (Exception e) {
			throw new UnprocessableEntityException(Error.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION,
				Error.UNPROCESSABLE_KAKAO_SERVER_EXCEPTION.getMessage());
		}
	}
}
