package com.myassign.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {

	//public static final int expireTime = 1000*60*60*2; // 2 hour
	
	public static final int expireTime = 1000*60; //60 seconds
	
	public static final String secret = "mytoken";
	
	private String type;

	private String token;

	private Date issueDate;
	
	private Date expireDate;
	
	private String scope;
}
