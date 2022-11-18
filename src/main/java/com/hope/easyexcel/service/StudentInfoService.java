package com.hope.easyexcel.service;

import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.dto.StudentInfoQuery;
import com.hope.easyexcel.dto.StudentInsert;
import com.hope.easyexcel.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件名：StudentInfoService
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:25
 * 描述：
 */
public interface StudentInfoService {
    /**
     * 获取学生信息
     */
    List<Student> getStudentByQueryInfo(StudentInfoQuery query);

    void saveExcelData(List<StudentExcelData> list) throws Exception;

    List<StudentExcelData> saveStudentExcelData(MultipartFile[] files) throws Exception;


}
