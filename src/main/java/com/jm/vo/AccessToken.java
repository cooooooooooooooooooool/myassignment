package com.jm.vo;

import java.util.Date;

public class AccessToken {

	//public static final int expireTime = 1000*60*60*2; // 2 hour
	
	public static final int expireTime = 1000*10; //10 seconds
	
	public static final String secret = "mytoken";
	
	private String type;

	private String token;

	private Date issueDate;
	
	private Date expireDate;
	
	private String scope;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccessToken [type=").append(type).append(", token=").append(token).append(", issueDate=").append(issueDate).append(", expireDate=").append(expireDate).append(", scope=").append(scope).append("]");
		return builder.toString();
	}
}
