package com.jm.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.entity.FinanceStatus;
import com.jm.entity.Institute;
import com.jm.exception.InstituteNotFoundException;
import com.jm.repository.FinanceStatusRepository;
import com.jm.repository.InstituteRepository;
import com.jm.vo.FinanceStatVo;
import com.jm.vo.InstituteAmountVo;
import com.jm.vo.InstituteAvgAmountVo;
import com.jm.vo.InstituteAvgMinMaxAmountVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InstituteService {
    
    @Autowired
    private InstituteRepository instituteRepository;
    
    @Autowired
    private FinanceStatusRepository financeStatusRepository;
    
    @Autowired
	@Qualifier("instituteCsvHeaderMap")
    private Map<String, String> instituteCsvHeaderMap;
    
    @Autowired
	@Qualifier("instituteNameMap")
    private Map<String, String> instituteNameMap;
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=RuntimeException.class)
    public void initFinanceStatuses() throws RuntimeException, IOException {
    	
    	Iterator<String> keyset = instituteCsvHeaderMap.keySet().iterator();
		List<String> keys = IteratorUtils.toList(keyset); 
		
		List<Institute> list = new ArrayList<>();
		for (String key : keys) {
			list.add(Institute.builder().code(instituteNameMap.get(instituteCsvHeaderMap.get(key))).name(instituteCsvHeaderMap.get(key)).build());
		}
    	
    	// CSV 로부터 데이터 초기화
		InputStream is = getClass().getClassLoader().getResourceAsStream("data.csv");
		Reader reader = new InputStreamReader(is, "euc-kr");

		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader("연도", "월", "주택도시기금1)(억원)", "국민은행(억원)", "우리은행(억원)", "신한은행(억원)", "한국시티은행(억원)", "하나은행(억원)", "농협은행/수협은행(억원)", "외환은행(억원)", "기타은행(억원)")
                .withIgnoreHeaderCase()
                .withTrim());
		
		List<FinanceStatus> financeList = new ArrayList<>();
		try {
			for (CSVRecord csvRecord : csvParser) {

				if (csvRecord.getRecordNumber()>1) {
					
		            int year = Integer.parseInt(csvRecord.get("연도"));
		            int month = Integer.parseInt(csvRecord.get("월"));
		            
		            for (String key : keys) {
		            	log.info(csvRecord.get(key));
		            	log.info(instituteCsvHeaderMap.get(key));
		            	financeList.add(FinanceStatus.builder().year(year).month(month).code(instituteCsvHeaderMap.get(key)).amount(Long.parseLong(csvRecord.get(key).replaceAll(",", ""))).build());
					}
				}
	        }
		} finally {
			csvParser.close();
		}
		
		// 주택 금융 공급 현황 저장
		if (financeList!=null && financeList.size()>0) {
			log.info("Institute list insert!");
	    	financeStatusRepository.saveAll(financeList);
		}
    }
    
    /**
	 * 전체 은행 코드 목록을 조회
	 */
    public List<Institute> getInstitutes() throws RuntimeException {
    	return instituteRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
    
    /**
	 * 연도별 지원 금액 합계를 조회
	 */
    @Transactional(readOnly=true)
    public List<FinanceStatVo> getTotalAmountList() throws RuntimeException {
    	List<FinanceStatVo> list = financeStatusRepository.getTotalAmountList();
    	
    	for (FinanceStatVo financeStat : list) {
    		List<InstituteAmountVo> institueAmountList = financeStatusRepository.getInstituteAmountList(financeStat.getYear());
    		
    		if (institueAmountList!=null && institueAmountList.size()>0) {
	    		Map<String, Long> detailAmountMap = new HashMap<>();
	    		for (InstituteAmountVo insAmount : institueAmountList) {
	    			detailAmountMap.put(insAmount.getInstituteName(), insAmount.getTotalAmount());
	    		}
	    		financeStat.setDetailAmount(detailAmountMap);
    		}
    	}
    	return list;
    }
    
    /**
	 * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 조회
	 */
    @Transactional(readOnly=true)
    public List<FinanceStatVo> getMaxAmountInstitueList() throws RuntimeException {
    	
    	List<FinanceStatVo> list = financeStatusRepository.getTotalAmountList();
    	
    	for (FinanceStatVo financeStat : list) {
    		List<InstituteAmountVo> institueAmountList = financeStatusRepository.getInstituteAmountList(financeStat.getYear());
    		
    		Map<String, Long> detailAmountMap = new HashMap<>();
    		for (InstituteAmountVo insAmount : institueAmountList) {
    			detailAmountMap.put(insAmount.getInstituteName(), insAmount.getTotalAmount());
    		}
    		
    		String key = Collections.max(detailAmountMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    		log.info("key : " + key + ", value : " + detailAmountMap.get(key));
    		financeStat.setInstituteName(key);
    	}
    	return list;
    }
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=RuntimeException.class)
    public void saveInstitutes(List<Institute> list) throws RuntimeException {
    	log.info("Institute list insert!");
    	instituteRepository.saveAll(list);
    }
    
    @Transactional(readOnly=true)
    public void saveFinanceStatus(FinanceStatus financeStatus) throws RuntimeException {
    	financeStatusRepository.save(financeStatus);
    }
    
    @Transactional(readOnly=true)
    public Institute getInstituteByName(String instituteName) throws RuntimeException {
    	return instituteRepository.findOneByName(instituteName);
    }
    
    /**
	 * 각 년도별 특정 기관의 지원 금액 평균값을 조회
	 */
	@SuppressWarnings("serial")
	@Transactional(readOnly=true)
    public InstituteAvgMinMaxAmountVo getInstituteAvgMinMaxAmount(String instituteName) throws RuntimeException {
    	
    	Institute institute = instituteRepository.findOneByName(instituteName);
    	
    	if (institute == null) throw new InstituteNotFoundException(instituteName);
    	
    	List<InstituteAvgAmountVo> list = financeStatusRepository.getInstituteAvgAmountList(institute.getCode());
    	
    	Map<Integer, Long> avgAmountMap = new HashMap<>();
		for (InstituteAvgAmountVo insAmount : list) {
			avgAmountMap.put(insAmount.getYear(), insAmount.getAvgAmount());
		}
		
		// 평균의 최소값 키
		int minKey = Collections.min(avgAmountMap.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		// 평균의 최대값 키
		int maxKey = Collections.max(avgAmountMap.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		List<Map<Integer, Long>> minMaxList = new ArrayList<>();
		
		minMaxList.add(new HashMap<Integer, Long>() {{
		    put(minKey, avgAmountMap.get(minKey));
		}});
		
		minMaxList.add(new HashMap<Integer, Long>() {{
		    put(maxKey, avgAmountMap.get(maxKey));
		}});
		
		log.info("minMaxList : " + minMaxList);
		
		return InstituteAvgMinMaxAmountVo.builder().instituteName(instituteName).supportAmount(minMaxList).build();
    }
}