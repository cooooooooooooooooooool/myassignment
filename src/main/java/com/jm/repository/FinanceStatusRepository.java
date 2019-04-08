package com.jm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jm.entity.FinanceStatus;
import com.jm.vo.FinanceStatVo;
import com.jm.vo.InstituteAmountVo;
import com.jm.vo.InstituteAvgAmountVo;

public interface FinanceStatusRepository extends JpaRepository<FinanceStatus, String> {

	@Query("SELECT new com.jm.vo.FinanceStatVo(fs.year, SUM(fs.amount) as totalAmount) FROM FinanceStatus fs "
			+ "GROUP BY fs.year ORDER BY fs.year")
	public List<FinanceStatVo> getTotalAmountList();
	
	@Query("SELECT new com.jm.vo.InstituteAmountVo(i.name, SUM(fs.amount) as totalAmount) FROM FinanceStatus fs LEFT OUTER JOIN Institute i ON fs.code = i.code "
			+ "WHERE fs.year = :year GROUP BY fs.code")
	public List<InstituteAmountVo> getInstituteAmountList(@Param("year") int year);
	
	@Query("SELECT new com.jm.vo.InstituteAvgAmountVo(fs.year, i.name, CAST(ROUND(AVG(fs.amount), 0) as long) as avgAmount) FROM FinanceStatus fs LEFT OUTER JOIN Institute i ON fs.code = i.code "
			+ "WHERE fs.code = :code GROUP BY fs.year ORDER BY fs.year")
	public List<InstituteAvgAmountVo> getInstituteAvgAmountList(@Param("code") String code);
}