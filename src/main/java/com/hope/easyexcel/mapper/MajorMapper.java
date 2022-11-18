package com.hope.easyexcel.mapper;

import com.hope.easyexcel.entity.Major;
import org.springframework.stereotype.Repository;

/**
 * 文件名：MajorMapper
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:18
 * 描述：
 */
@Repository
public interface MajorMapper {
    /**
     * 根据id来查找专业信息
     * @param id id
     * @return academy's info
     */
    Major findMajorBaseInfoById(Integer id);

    Integer findMajorIdByName(String majorName);
}
