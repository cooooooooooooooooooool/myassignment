package com.jm.vo;

public class InstituteAvgAmountVo {
	
	private int year;

    private String instituteName;
    
    private long avgAmount;
    
    public InstituteAvgAmountVo() {}
    
    public InstituteAvgAmountVo(int year, String instituteName, long avgAmount) {
    	this.year = year;
    	this.instituteName = instituteName;
    	this.avgAmount = avgAmount;
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

	/**
	 * @return the avgAmount
	 */
	public long getAvgAmount() {
		return avgAmount;
	}

	/**
	 * @param avgAmount the avgAmount to set
	 */
	public void setAvgAmount(long avgAmount) {
		this.avgAmount = avgAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstituteAvgAmountVo [year=").append(year).append(", instituteName=").append(instituteName).append(", avgAmount=").append(avgAmount).append("]");
		return builder.toString();
	}
}
