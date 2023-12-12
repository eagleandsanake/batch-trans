package org.wx;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuxin
 * @date ${YEAR}/${MONTH}/${DAY} ${HOUR}:${MINUTE}:${SECOND}
 */
public class BatchTransCaller {
    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8080/echo";

        List<Pair<Object,Object>> pairs = new ArrayList<>() {{
            add(Pair.of("sourceNo", "111"));
            add(Pair.of("targetNo", "222"));
            add(Pair.of("transAmount", "20"));
        }};
        makeFormCall(null,pairs,url);

    }


    @ExcelIgnoreUnannotated
    @Data
    public static class TransModel{

        @ExcelProperty("出款方")
        private String sourceNo;

        @ExcelProperty("入款方")
        private String targetNo;

        @ExcelProperty(" 金额")
        private BigDecimal transAmount;

    }

    public static List<TransModel> readData(String fileUrl){
        File file = new File(fileUrl);
        List<TransModel> transModels = new ArrayList<>();
        EasyExcel.read(file, TransModel.class, new ReadListener<TransModel>() {
                    @Override
                    public void invoke(TransModel o, AnalysisContext analysisContext) {
                        transModels.add(o);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                    }
                })
                .sheet()
                .doRead();
        String.format("----已经读取数据共%s条！",transModels.size());
        return transModels;
    }

    public static void makeFormCall(List<Pair<Object,Object>> headers,
                                    List<Pair<Object,Object>> formData,
                                    String url) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        List<BasicNameValuePair> formParams = formData.stream().map(e -> {
            return new BasicNameValuePair(e.getLeft().toString(), e.getRight().toString());
        }).collect(Collectors.toList());

        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(formParams));
//        for (Pair<Object, Object> header : headers) {
//            httpPost.setHeader(header.getLeft().toString(), header.getRight());
//        }
        HttpResponse response = httpClient.execute(httpPost);
        String reasonPhrase = response.getReasonPhrase();
        System.err.println(reasonPhrase);

    }


}