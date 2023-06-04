package com.ays.imgurapi.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ays.imgurapi.dto.AlbumDto;

@Service
public interface ImgurService {
	public AlbumDto createUserAlbum(String useranme) throws IOException;
	public ResponseEntity <Object> upload(MultipartFile image, String Id, String username) throws IOException;
	public ResponseEntity <Object> retrieveUserImages(String albumId) throws IOException; 
}
