package com.hope.easyexcel.service.Impl;

import com.hope.easyexcel.entity.Classes;
import com.hope.easyexcel.mapper.AcademyMapper;
import com.hope.easyexcel.mapper.ClassMapper;
import com.hope.easyexcel.mapper.MajorMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 文件名：SchoolInfoServiceImpl
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-15:18
 * 描述：
 */
@Service
public class SchoolInfoServiceImpl {

    private AcademyMapper academyMapper;
    private MajorMapper majorMapper;
    private ClassMapper classMapper;

    @Autowired
    public SchoolInfoServiceImpl(AcademyMapper academyMapper, MajorMapper majorMapper, ClassMapper classMapper) {
        this.academyMapper = academyMapper;
        this.majorMapper = majorMapper;
        this.classMapper = classMapper;
    }


    /**
     * 获取学院名
     * @param academyName
     * @return
     */
    public Integer getAcademyIdByName(String academyName) {
        return academyMapper.findAcademyIdByName(academyName);
    }


    /**
     * 获取专业名
     * @param majorName
     * @return
     */
    public Integer getMajorIdByName(String majorName) {
        return majorMapper.findMajorIdByName(majorName);
    }


    /**
     * 获取班级名
     * @param majorName
     * @param className
     * @return
     */
    public Integer getClassIdByName(String majorName, String className) {
        return classMapper.findClassIdByMajorNameAndClassName(majorName, className);
    }

    /**
     * 查询班级的人数 count
     * @param classId
     * @return
     */
    public Integer getClassSize(Integer classId) {
        return classMapper.findClassSizeByClassId(classId);
    }

    /**
     * 每次插入是对班级的count ++
     * @param classSizeMap
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateClassSize(Map<Integer, Integer> classSizeMap) {
        for (Map.Entry<Integer, Integer> entry : classSizeMap.entrySet()) {
            classMapper.updateClassSize(entry.getKey(), entry.getValue());
        }
    }
}
