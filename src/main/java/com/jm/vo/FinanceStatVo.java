package com.jm.vo;

import java.util.Map;

public class FinanceStatVo {

    private int year;
    
    private long totalAmount;
    
    private String instituteName;

    private Map<String, Long> detailAmount;
    
    public FinanceStatVo() {}
    
    public FinanceStatVo(int year, long totalAmount) {
    	this.year = year;
    	this.totalAmount = totalAmount;
    }

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
	 * @return the totalAmount
	 */
	public long getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the detailAmount
	 */
	public Map<String, Long> getDetailAmount() {
		return detailAmount;
	}

	/**
	 * @param detailAmount the detailAmount to set
	 */
	public void setDetailAmount(Map<String, Long> detailAmount) {
		this.detailAmount = detailAmount;
	}

	/**
	 * @return the instituteName
	 */
	public String getInstituteName() {
		return instituteName;
	}

	/**
	 * @param instituteName the instituteName to set
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinanceStatVo [year=").append(year).append(", totalAmount=").append(totalAmount).append(", instituteName=").append(instituteName).append(", detailAmount=").append(detailAmount).append("]");
		return builder.toString();
	}
}
