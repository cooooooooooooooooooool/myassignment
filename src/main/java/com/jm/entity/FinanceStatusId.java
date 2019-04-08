package com.jm.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class FinanceStatusId implements Serializable {
	
	private static final long serialVersionUID = 2047933882727173365L;

	@Id
    private int year;
    
    @Id
    private int month;
    
    @Id
    private String code;

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinanceStatusId [year=").append(year).append(", month=").append(month).append(", code=").append(code).append("]");
		return builder.toString();
	}
}