package com.hope.easyexcel.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 文件名：StatusConverter
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/16-00:52
 * 描述：学籍状态自定义转换器
 */
public class StatusConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }


    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return "在学".equals(cellData.getStringValue()) ? "1" : "1";
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        //0 在学 1 辍学
//        if (value == null) {
//            return new WriteCellData("");
//        } else if (value == 0) {
//            return new WriteCellData("在学");
//        } else if (value == 1) {
//            return new WriteCellData("辍学");
//        }

        return new WriteCellData(value.equals("1") ? "在学" : "辍学");

    }

}
