package com.ays.imgurapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ays.imgurapi.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, String> {
	Optional<Album> findById(String Id);

}
