package com.myassign.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.model.dto.FinanceStatVo;
import com.myassign.model.dto.InstituteAvgMinMaxAmountVo;
import com.myassign.model.entity.FinanceStatus;
import com.myassign.model.entity.Institute;
import com.myassign.model.repository.FinanceStatusRepository;
import com.myassign.model.repository.InstituteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstituteService {

    private final InstituteRepository instituteRepository;

    private final FinanceStatusRepository financeStatusRepository;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void initFinanceStatuses() throws IOException {
    }

    /**
     * 전체 은행 코드 목록을 조회
     */
    public List<Institute> getInstitutes() {
        return instituteRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    /**
     * 연도별 지원 금액 합계를 조회
     */
    @Transactional(readOnly = true)
    public List<FinanceStatVo> getTotalAmountList() {
        /*
         * List<FinanceStatVo> list = financeStatusRepository.getTotalAmountList();
         * 
         * for (FinanceStatVo financeStat : list) { List<InstituteAmountVo>
         * institueAmountList =
         * financeStatusRepository.getInstituteAmountList(financeStat.getYear());
         * 
         * if (institueAmountList != null && institueAmountList.size() > 0) {
         * Map<String, Long> detailAmountMap = new HashMap<>(); for (InstituteAmountVo
         * insAmount : institueAmountList) {
         * detailAmountMap.put(insAmount.getInstituteName(),
         * insAmount.getTotalAmount()); } financeStat.setDetailAmount(detailAmountMap);
         * } }
         */
        return null;
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 조회
     */
    @Transactional(readOnly = true)
    public List<FinanceStatVo> getMaxAmountInstitueList() {
        /*
         * List<FinanceStatVo> list = financeStatusRepository.getTotalAmountList();
         * 
         * for (FinanceStatVo financeStat : list) { List<InstituteAmountVo>
         * institueAmountList =
         * financeStatusRepository.getInstituteAmountList(financeStat.getYear());
         * 
         * Map<String, Long> detailAmountMap = new HashMap<>(); for (InstituteAmountVo
         * insAmount : institueAmountList) {
         * detailAmountMap.put(insAmount.getInstituteName(),
         * insAmount.getTotalAmount()); }
         * 
         * String key = Collections.max(detailAmountMap.entrySet(),
         * Map.Entry.comparingByValue()).getKey(); log.info("key : " + key +
         * ", value : " + detailAmountMap.get(key)); financeStat.setInstituteName(key);
         * }
         */
        return null;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveInstitutes(List<Institute> list) {
        log.info("Institute list insert!");
        instituteRepository.saveAll(list);
    }

    @Transactional(readOnly = true)
    public void saveFinanceStatus(FinanceStatus financeStatus) {
        financeStatusRepository.save(financeStatus);
    }

    @Transactional(readOnly = true)
    public Institute getInstituteByName(String instituteName) {
        return instituteRepository.findOneByName(instituteName);
    }

    /**
     * 각 년도별 특정 기관의 지원 금액 평균값을 조회
     */
    @Transactional(readOnly = true)
    public InstituteAvgMinMaxAmountVo getInstituteAvgMinMaxAmount(String instituteName) {

        /*
         * Institute institute = instituteRepository.findOneByName(instituteName);
         * 
         * if (institute == null) throw new InstituteNotFoundException(instituteName);
         * 
         * List<InstituteAvgAmountVo> list =
         * financeStatusRepository.getInstituteAvgAmountList(institute.getCode());
         * 
         * Map<Integer, Long> avgAmountMap = new HashMap<>(); for (InstituteAvgAmountVo
         * insAmount : list) { avgAmountMap.put(insAmount.getYear(),
         * insAmount.getAvgAmount()); }
         * 
         * // 평균의 최소값 키 int minKey = Collections.min(avgAmountMap.entrySet(),
         * Map.Entry.comparingByValue()).getKey();
         * 
         * // 평균의 최대값 키 int maxKey = Collections.max(avgAmountMap.entrySet(),
         * Map.Entry.comparingByValue()).getKey();
         * 
         * List<Map<Integer, Long>> minMaxList = new ArrayList<>();
         * 
         * minMaxList.add(new HashMap<Integer, Long>() { { put(minKey,
         * avgAmountMap.get(minKey)); } });
         * 
         * minMaxList.add(new HashMap<Integer, Long>() { { put(maxKey,
         * avgAmountMap.get(maxKey)); } });
         * 
         * log.info("minMaxList : " + minMaxList);
         */

        return null;
    }
}