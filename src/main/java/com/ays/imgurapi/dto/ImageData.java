package com.ays.imgurapi.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ImageData {
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonProperty("id")
	public String id;
	@JsonProperty("title")
	public String title; 
	@JsonProperty("images")
	public ArrayList<Image> images;
}
