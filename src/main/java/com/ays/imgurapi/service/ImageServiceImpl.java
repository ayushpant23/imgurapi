package com.ays.imgurapi.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ays.imgurapi.dto.AlbumDto;
import com.ays.imgurapi.entity.Album;
import com.ays.imgurapi.repository.AlbumRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	AlbumRepository albumRepository;

	@Override
	public String saveAlbum(AlbumDto albumDto) {
		log.info("Album ID: {}", albumDto.getId());
		Album album = new Album();
		album.setId(albumDto.getId());
		album.setDeletehash(albumDto.getDeleteHash());
		album.setUsername(albumDto.getUsername());
		albumRepository.save(album);
		log.info("Album has created against :  {}", albumDto.getId());
		return "";
	}

	@Override
	public AlbumDto findall(String username) {
		AlbumDto albumDto = new AlbumDto();

		List<Album> list = albumRepository.findAll();
		List<Album> alist = list.stream().filter(a-> a.getUsername().equals(username)).collect(Collectors.toList());

		albumDto.setImages(alist.get(0).getImages());
		albumDto.setId(alist.get(0).getId());
		albumDto.setAlbumName(alist.get(0).getAlbumName());
		albumDto.setUsername(alist.get(0).getUsername());
		log.info("Album ID {} ",alist.get(0).getId());

		return albumDto;
	}

}
