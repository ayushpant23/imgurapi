package com.ays.imgurapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity 
@Table(name = "Images")
public class Images {
	@Id
	private String Id;
    
	@ManyToOne
    @JoinColumn(name="album_id", nullable=true)
    private Album album;
	
}
