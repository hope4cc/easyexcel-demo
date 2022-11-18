package com.hope.easyexcel.mapper;

import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.dto.StudentInfoQuery;
import com.hope.easyexcel.dto.StudentInsert;
import com.hope.easyexcel.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名：StudentMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:14
 * 描述：
 */
@Repository
public interface StudentMapper {
    /**
     * 根据查询条件来查询学生基础信息（除去课程、考试信息）
     * @param query 查询条件
     * @return 学生信息
     */
    List<Student> findStudentByCondition(StudentInfoQuery query);
    /**
     * 批量插入学生信息
     * @param studentExcelData 学生信息Excel对应的实体类
     * @return effect rows
     */
    int insertBatchStudentInfos(@Param("studentExcelData") List<StudentExcelData> studentExcelData);

    void insertOneStudentInfo(StudentInsert studentInsert);
}
