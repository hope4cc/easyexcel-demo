package com.hope.easyexcel.mapper;

import com.hope.easyexcel.entity.Classes;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 文件名：ClassMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:18
 * 描述：
 */
@Repository
public interface ClassMapper {
    /**
     * 根据id来查找班级信息
     * @param id id
     * @return class's info
     */
    Classes findClassBaseInfoById(Integer id);
    int updateClassSizePlusOne(Integer classId);

    Integer findClassIdByMajorNameAndClassName(String majorName, String className);

    Integer findClassSizeByClassId(Integer classId);

    void updateClassSize(@Param("classId") Integer key, @Param("classSize") Integer value);
}
