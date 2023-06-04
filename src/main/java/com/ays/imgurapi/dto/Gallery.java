package com.ays.imgurapi.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Gallery {
	 @JsonIgnoreProperties(ignoreUnknown = true)
	  @JsonProperty("data")
	  public ArrayList<ImageData> data;
}
