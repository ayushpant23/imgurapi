package com.ays.imgurapi.service;

import org.springframework.stereotype.Service;

import com.ays.imgurapi.dto.AlbumDto;

@Service
public interface ImageService {
	
	public String saveAlbum(AlbumDto albumDto);
	public AlbumDto findall(String username);
}
