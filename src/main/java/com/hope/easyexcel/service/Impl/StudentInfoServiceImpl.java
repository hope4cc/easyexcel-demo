package com.hope.easyexcel.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.dto.StudentInfoQuery;
import com.hope.easyexcel.dto.StudentInsert;

import com.hope.easyexcel.entity.Classes;
import com.hope.easyexcel.entity.Student;
import com.hope.easyexcel.exception.SchoolInfoNotFoundException;
import com.hope.easyexcel.exception.StudentInfoInsertException;
import com.hope.easyexcel.mapper.StudentMapper;
import com.hope.easyexcel.service.StudentInfoService;
import com.hope.easyexcel.utils.StudentDataListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件名：SchoolInfoServiceImpl
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:25
 * 描述：
 */
@Service
@Slf4j
public class StudentInfoServiceImpl implements StudentInfoService {
    private StudentMapper studentMapper;
    private SchoolInfoServiceImpl schoolInfoService;

    private final Map<Integer, Integer> classSizeMap = new ConcurrentHashMap<>();

    /**
     * 错误数据集合
     */
    @Getter
    private final List<StudentExcelData> errorList = new ArrayList<>();

    @Autowired
    public StudentInfoServiceImpl(StudentMapper studentMapper, SchoolInfoServiceImpl schoolInfoService) {
        this.studentMapper = studentMapper;
        this.schoolInfoService = schoolInfoService;
    }

    /**
     * 获取学生信息
     * @param query
     * @return
     */
    @Override
    public List<Student> getStudentByQueryInfo(StudentInfoQuery query) {
        return studentMapper.findStudentByCondition(query);
    }


    @Override
    public   void saveExcelData(List<StudentExcelData> list) throws Exception {
        if (errorList.size() > 0) {
            list.removeAll(errorList);
        }
        try {
            //调用加工学生信息
            processStudentExcelData(list);
            studentMapper.insertBatchStudentInfos(list);
            List<Student> students = new ArrayList<>();
            for (StudentExcelData excelData : list) {
                students.add(new Student(excelData.getId(), excelData.getName(), excelData.getIdCard(), excelData.getEmail()));
            }
        } catch (Exception e) {
            log.info("存在错误信息的学生信息集合：{}", errorList);
            log.info("学生信息添加异常集合：{}", list);
            log.info("异常信息为：{}", e.getMessage());

            errorList.addAll(list);
            throw new StudentInfoInsertException();
        }
    }

    /**
     * 错误信息列表
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    public  List<StudentExcelData> saveStudentExcelData(MultipartFile[] files) throws Exception {
        errorList.clear();
        List<StudentExcelData> res = new ArrayList<>();
        for (MultipartFile file : files) {
            EasyExcel.read(file.getInputStream(), StudentExcelData.class, new StudentDataListener(this))
                    .sheet().doRead();
            res.addAll(errorList);
            errorList.clear();
        }
        return res;
    }


    /**
     * 处理学生信息
     * @param dataList
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public  void  processStudentExcelData(List<StudentExcelData> dataList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        boolean existError = false;
        for (StudentExcelData excelData : dataList) {
            try {
                //获取相关学院、专业、班级信息(id)
                Integer academyId = schoolInfoService.getAcademyIdByName(excelData.getAcademyName());
                Integer majorId = schoolInfoService.getMajorIdByName(excelData.getMajorName());
                Integer classId = schoolInfoService.getClassIdByName(excelData.getMajorName(), excelData.getClassName());
                if (academyId == null || majorId == null || classId == null) {
                    throw new SchoolInfoNotFoundException();
                } else {
                    excelData.setAcademyId(academyId);
                    excelData.setMajorId(majorId);
                    excelData.setClassId(classId);
                }
                //生成相应的学工号
                String year = dateFormat.format(excelData.getCreateTime());
                String className = excelData.getClassName();
                // id  2022 22
                //      学院id + 专业id + 班级名-2
                String id = year.substring(year.length() - 2) +
                        academyId +
                        majorId +
                        className.substring(className.length() - 2);
                int count = getClassPersonAndIncrement(classId);
                if (count < 9) {
                    id += "0" + count;
                } else {
                    id += count;
                }
                excelData.setId(id);
            } catch (SchoolInfoNotFoundException e) {
                errorList.add(excelData);
                existError = true;
            }
            //最后更新相关的班级人数
            if (!existError) {
                schoolInfoService.updateClassSize(classSizeMap);
            }
        }
    }

    private int getClassPersonAndIncrement(Integer classId) {
        if (classSizeMap.containsKey(classId)) {
            classSizeMap.put(classId, classSizeMap.get(classId) + 1);
        } else {
            classSizeMap.put(classId, schoolInfoService.getClassSize(classId) + 1);
        }
        return classSizeMap.get(classId);
    }

}
