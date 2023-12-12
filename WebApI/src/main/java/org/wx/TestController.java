package org.wx;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author wuxin
 * @date 2023/12/12 22:23:54
 */
@RestController
public class TestController {

    @PostMapping("/echo")
    public TransModel echo(String sourceNo, String targetNo, BigDecimal transAmount){
        TransModel transModel = new TransModel();
        transModel.setTransAmount(transAmount);
        transModel.setTargetNo(targetNo);
        transModel.setSourceNo(sourceNo);
        System.out.println(transModel);
        return transModel;
    }

    @Data
    public static class TransModel{

        @ExcelProperty("出款方")
        private String sourceNo;

        @ExcelProperty("入款方")
        private String targetNo;

        @ExcelProperty(" 金额")
        private BigDecimal transAmount;

    }





}
