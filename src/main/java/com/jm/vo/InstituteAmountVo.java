package com.jm.vo;

public class InstituteAmountVo {

    private String instituteName;
    
    private long totalAmount;
    
    public InstituteAmountVo() {}
    
    public InstituteAmountVo(String instituteName, long totalAmount) {
    	this.instituteName = instituteName;
    	this.totalAmount = totalAmount;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinanceStatDetailVo [instituteName=").append(instituteName).append(", totalAmount=").append(totalAmount).append("]");
		return builder.toString();
	}
}
