package com.ays.imgurapi.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Image implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonProperty("id")
	public String id;
	@JsonProperty("link")
	public String link;
	@JsonProperty("mp4")
	public String mp4;
	@JsonProperty("gifv")
	public String gifv;
}