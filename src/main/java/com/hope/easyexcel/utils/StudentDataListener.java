package com.hope.easyexcel.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson2.JSON;
import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.exception.StudentInfoInsertException;
import com.hope.easyexcel.service.Impl.StudentInfoServiceImpl;
import com.hope.easyexcel.service.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件名：FastJsonRedisSerializer
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:46
 * 描述：监听器
 */
@Slf4j
public class StudentDataListener extends AnalysisEventListener<StudentExcelData> {
    //定义接收异常
    private StringBuffer  errorMsg;
    /**
     * 每隔200条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 200;
    private final List<StudentExcelData> list = new ArrayList<>();
    private StudentInfoService studentInfoService;
    public StudentDataListener(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    // 错误行号
    private String error = "";
    /**
     * 每一条数据解析都会调用该方法
     * @param studentExcelData 一行的value
     * @param analysisContext
     */
    @Override
    public void invoke(StudentExcelData studentExcelData, AnalysisContext analysisContext) {
        log.info("解析到一条excel数据：{}", JSON.toJSONString(studentExcelData));
        Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
        System.out.println("读取" + rowIndex + "行数据");


        String errorMessage = null;
        try {
            errorMessage = ExcelValidateHelper.validateEntity(studentExcelData);
        } catch (NoSuchFieldException e) {
            errorMessage = "解析数据出错";
            e.printStackTrace();
        }
        list.add(studentExcelData);
        if (StringUtils.isEmpty(errorMessage)) {
            log.info("list 添加 ");
            list.add(studentExcelData);
        } else {
            studentExcelData.setDescription(errorMessage);
            ((StudentInfoServiceImpl) studentInfoService).getErrorList().add(studentExcelData);
        }

        //达到BATCH_COUNT，需要存储一次数据库，防止几万条数据在内存中，出现OOM

        if (list.size() >= BATCH_COUNT) {
            try {
                log.info("超过10条 BATCH_COUNT");
                studentInfoService.saveExcelData(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.clear();
        }
    }



    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
            String msg="第"+excelDataConvertException.getRowIndex()+"行，第"+excelDataConvertException.getColumnIndex()+"列解析异常，数据为:"+ excelDataConvertException.getCellData();
            if(errorMsg==null){
                errorMsg=new StringBuffer();
            }
            //拼接报错信息
            errorMsg.append(msg).append(System.lineSeparator());
        }

    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JSON.toJSONString(headMap));

    }

    /**
     * 所有数据都被解析完成，会调用该方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            if (list.size() > 0) {
                studentInfoService.saveExcelData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("所有数据已被解析完成！");

        list.clear();
    }

}
