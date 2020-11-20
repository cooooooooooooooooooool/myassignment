package com.jm.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jm.model.dto.InstituteAvgMinMaxAmountVo;
import com.jm.model.entity.Institute;
import com.jm.service.InstituteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstitueTest {

    private static final Logger logger = LoggerFactory.getLogger(InstitueTest.class);

    // @Autowired
    // private RestTemplate restTemplate;

    @Autowired
    private InstituteService instituteService;

    @Test
    public void getInstituteTest() {
        Institute institute = instituteService.getInstituteByName("외환은행");
        assertThat(institute).isNotNull();
        logger.info("institute : " + institute);
    }

    @Test
    public void getInstituteAvgMinMaxAmountTest() {
        String instituteName = "외환은행";
        InstituteAvgMinMaxAmountVo instituteAvgMinMaxAmount = instituteService.getInstituteAvgMinMaxAmount(instituteName);
        logger.info("instituteAvgMinMaxAmount : " + instituteAvgMinMaxAmount);
    }

    @Test
    public void readCsvFileTest() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream("data-test.csv");
        Reader reader = new InputStreamReader(is, "euc-kr");

        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("연도", "월", "주택도시기금1)(억원)", "국민은행(억원)", "우리은행(억원)", "신한은행(억원)", "한국시티은행(억원)", "하나은행(억원)", "농협은행/수협은행(억원)", "외환은행(억원)", "기타은행(억원)").withIgnoreHeaderCase().withTrim());

        try {
            for (CSVRecord csvRecord : csvParser) {

                String year = csvRecord.get("연도");
                String month = csvRecord.get("월");
                String etc = csvRecord.get("기타은행(억원)");

                logger.info("Record No - " + csvRecord.getRecordNumber());
                logger.info("---------------");
                logger.info("연 : " + year);
                logger.info("월 : " + month);
                logger.info("기타은행(억원) : " + etc);
                logger.info("---------------\n\n");
            }
        } finally {
            csvParser.close();
        }
    }
}
