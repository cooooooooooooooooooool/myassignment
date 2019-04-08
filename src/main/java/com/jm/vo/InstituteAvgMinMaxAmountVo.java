package com.jm.vo;

import java.util.List;
import java.util.Map;

public class InstituteAvgMinMaxAmountVo {

    private String instituteName;
    
    private List<Map<Integer, Long>> supportAmount;
    
    public InstituteAvgMinMaxAmountVo() {}
    
    public InstituteAvgMinMaxAmountVo(String instituteName, List<Map<Integer, Long>> supportAmount) {
    	this.instituteName = instituteName;
    	this.supportAmount = supportAmount;
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
	 * @return the supportAmount
	 */
	public List<Map<Integer, Long>> getSupportAmount() {
		return supportAmount;
	}

	/**
	 * @param supportAmount the supportAmount to set
	 */
	public void setSupportAmount(List<Map<Integer, Long>> supportAmount) {
		this.supportAmount = supportAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstituteAvgMinMaxAmountVo [instituteName=").append(instituteName).append(", supportAmount=").append(supportAmount).append("]");
		return builder.toString();
	}
}
