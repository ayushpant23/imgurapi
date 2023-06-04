package com.ays.imgurapi.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.ays.imgurapi.dto.AlbumDto;
import com.ays.imgurapi.dto.ImgurData;
import com.ays.imgurapi.entity.Album;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ImgurServiceImpl implements ImgurService{
	private final ImgurData imgurData;
	@Autowired
	private RestTemplate restTemplate;

	/*
	 * This method has implemented for OAuth which will be open in new window 
	 */

	public RedirectView auth() throws UnsupportedEncodingException{

		ResponseEntity<String> response = null;
		System.out.println("Authorization code------" + imgurData.getClientId());
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();		
		String redirectedUrl= URLEncoder.encode( "https://oauth.pstmn.io/v1/callback", "UTF-8" );  
		String access_token_url = imgurData.getAuthorizeUrl();
		access_token_url += "?response_type=code";
		access_token_url += "&client_id=fe22b7f5b3b0150";
		access_token_url += "&redirect_uri="+redirectedUrl;
		System.out.println(access_token_url);
		response = restTemplate.getForEntity(access_token_url, String.class, headers);
		return new RedirectView(access_token_url);
	}

	/*
	 * This method has implemented save the refresh token which will be coming from fallback before processing refresh token
	 * 
	 */

	public ResponseEntity <Object> saveToken(@RequestParam (name = "code") String code){
		log.info("Refresh token  fethched from Imgur is : {}", code );
		imgurData.setRefreshToken(code);
		return ResponseEntity.ok().build();
	}

	/*
	 * This method has implemented for upload an image 
	 * File type MultipartFile image
	 */

	@Override
	public ResponseEntity <Object> upload(MultipartFile image, String albumId, String username) throws IOException {
		refreshToken();
		String accessToken = imgurData.getAccessToken();
		if(Strings.isBlank(accessToken))
			throw new RuntimeException("Access token not found");

		String base64Img = Base64.getEncoder().encodeToString(image.getBytes());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		header.add("Authorization", "Bearer " + accessToken);
		Timestamp tmp =  new  Timestamp(System.currentTimeMillis());
		log.info(tmp.getNanos()+"");
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("title",username+""+tmp.getNanos());
		body.add("image", base64Img);
		body.add("description", "Image uploaded by "+username);
		body.add("type", "base64");
		body.add("album", albumId);
		log.info(body.toString());
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
		ResponseEntity<Map> response = restTemplate.postForEntity(imgurData.getUploadUrl(), request, Map.class);
		LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
		LinkedHashMap<String, Object> res = (LinkedHashMap<String, Object>) resBody.get("data");
		log.info(res.toString());
		return ResponseEntity.ok().body(res);
	}

	/*
	 * This method is created for refresh the token from Imgur
	 * Method will return the access token with refresh token
	 * 
	 */

	public ResponseEntity<LinkedHashMap<String, Object>> refreshToken() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		System.out.println(imgurData.toString());
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("refresh_token", imgurData.getRefreshToken());
		body.add("client_id", imgurData.getClientId());
		body.add("client_secret", imgurData.getClientSecret());
		body.add("grant_type", "refresh_token");
		System.out.println(body.toString());
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
		ResponseEntity<Map> response = restTemplate.postForEntity(imgurData.getAccessTokenUrl(), request, Map.class);

		LinkedHashMap<String, Object> resBody = (LinkedHashMap<String, Object>) response.getBody();
		String newAccessToken = (String) resBody.get("access_token");
		String newRefreshToken = (String) resBody.get("refresh_token");
		imgurData.setAccessToken(newAccessToken);
		imgurData.setRefreshToken(newRefreshToken);
		return ResponseEntity.ok(resBody);
	}

	/* 
	 * Retrieve all the images associated with user  
	 */
	@GetMapping("/retrieve")
	public ResponseEntity <Object> retrieveAll() throws IOException {
		refreshToken();
		String accessToken = imgurData.getAccessToken();
		log.info("Access token were used : {} ", accessToken);
		if(Strings.isBlank(accessToken))
			throw new RuntimeException("Access token not found");
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(header);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(header);
		ResponseEntity<Map> response = restTemplate.exchange(imgurData.getImages(), HttpMethod.GET, entity, Map.class);
		Map<String,Object> resBody = (LinkedHashMap<String,Object>) response.getBody();
		Object res =  resBody.get("data");
		return ResponseEntity.ok().body(res);
	}

	/* 
	 * Retrieve image using id
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity <Object> retrieveUserImages(String albumId) throws IOException {
		refreshToken();
		log.info("AlbumId : {}", albumId);
		String accessToken = imgurData.getAccessToken();
		log.info("Access token were used : {} ", accessToken);
		if(Strings.isBlank(accessToken))
			throw new RuntimeException("Access token not found");
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", "Client-ID " + imgurData.getClientId());
		HttpEntity<String> entity = new HttpEntity<String>(header);
		String albumUrl= imgurData.getAlbumUrl()+"/"+albumId;
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(header);
		ResponseEntity<Map> response = restTemplate.exchange(albumUrl, HttpMethod.GET, entity, Map.class);
		Map<String,Object> resBody = (LinkedHashMap<String,Object>) response.getBody();
		Map<String,Object> res  =  (Map<String, Object>) resBody.get("data");
		return ResponseEntity.ok().body(res.get("images"));
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public AlbumDto createUserAlbum(String useranme) throws IOException {
		refreshToken();
		log.info("Create user Album : {}", useranme);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("title", useranme);
		body.add("description", "This album created by :"+useranme);
		body.add("privacy", "public");
		header.add("Authorization", "Client-ID " + imgurData.getClientId());
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, header);
		ResponseEntity<Map> response = restTemplate.postForEntity(imgurData.getAlbumUrl(), request, Map.class);
		Map<String,Object> resBody = (LinkedHashMap<String,Object>) response.getBody();
		Map<String,Object> res =  (Map<String, Object>) resBody.get("data");
		String id = (String) res.get("id");
		String deleteHash = (String) res.get("deletehash");
		AlbumDto albumDto = new AlbumDto();
		albumDto.setId(id);
		albumDto.setDeleteHash(deleteHash);
		albumDto.setUsername(useranme);
		return albumDto;
	}   

}