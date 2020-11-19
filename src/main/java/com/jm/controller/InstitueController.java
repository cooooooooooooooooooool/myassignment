package com.jm.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jm.model.entity.Institute;
import com.jm.service.InstituteService;
import com.jm.vo.FinanceStatVo;
import com.jm.vo.InstituteAvgMinMaxAmountVo;

@Validated
@RestController
@RequestMapping(value = "/banks")
public class InstitueController {

    @Autowired
    private InstituteService service;

    /**
     * 주택 공급 현황 데이터 초기화
     * 
     * @return
     * @throws IOException
     * @throws RuntimeException
     */
    @PostMapping("/init")
    public void initBankStatus() throws RuntimeException, IOException {
        service.initFinanceStatuses();
    }

    /**
     * 전체 은행 코드 목록을 조회
     * 
     * @return
     */
    @GetMapping
    public List<Institute> getBanks() {
        return service.getInstitutes();
    }

    /**
     * 연도별 지원 금액 합계를 조회
     * 
     * @return
     */
    @GetMapping("/sum")
    public List<FinanceStatVo> getTotalAmountList() {
        return service.getTotalAmountList();
    }

    /**
     * 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 조회
     * 
     * @return
     */
    @GetMapping("/max")
    public List<FinanceStatVo> getMaxAmountInstitueList() {
        return service.getMaxAmountInstitueList();
    }

    /**
     * 각 년도별 특정 기관의 지원 금액 평균값을 조회
     * 
     * @return
     * @throws RuntimeException
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/avg/{instituteName}")
    public InstituteAvgMinMaxAmountVo getInstituteAvgMinMaxAmount(@PathVariable(value = "instituteName") String instituteName) throws UnsupportedEncodingException, RuntimeException {
        return service.getInstituteAvgMinMaxAmount(URLDecoder.decode(instituteName, "UTF-8"));
    }
}