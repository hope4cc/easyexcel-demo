package com.hope.easyexcel.mapper;

import com.hope.easyexcel.entity.Academy;
import org.springframework.stereotype.Repository;

/**
 * 文件名：AcademyMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:19
 * 描述：
 */
@Repository
public interface AcademyMapper {
    /**
     * 根据id来查找学院信息
     * @param id id
     * @return academy's info
     */
    Academy findAcademyBaseInfoById(Integer id);
    Integer findAcademyIdByName(String academyName);
}
