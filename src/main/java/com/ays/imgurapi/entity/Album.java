package com.ays.imgurapi.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "album")
public class Album {

    @Id
    private String Id;
    
    @Column(length = 60)
    private String username; 
    
    @Column(length = 60)
    private String albumName;
    
    @Column(length = 60)
    private String albumDescription;
    
    @Column(length = 60)
    private String deletehash;
    
    @Column
    @OneToMany(mappedBy="album")
    private Set<Images> images;
}
