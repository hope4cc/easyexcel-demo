package com.hope.easyexcel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hope.easyexcel.comon.RespBean;
import com.hope.easyexcel.comon.ResponseCode;
import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.dto.StudentInfoQuery;
import com.hope.easyexcel.dto.StudentInsert;
import com.hope.easyexcel.entity.Student;
import com.hope.easyexcel.service.Impl.StudentInfoServiceImpl;

import com.hope.easyexcel.utils.StudentInfoInsertUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件名：StudentInfoController
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:28
 * 描述：/student
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentInfoController {
    private static StudentInfoServiceImpl studentInfoService;

    @Autowired
    public StudentInfoController(StudentInfoServiceImpl studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    /**
     * 健康测试
     * @return
     */
    @GetMapping("/hello")
    public String hello(){
        return "ok";
    }



    /**
     * 模板接口
     * @param response
     * @throws IOException
     */
    @GetMapping("/templateFile/download")
    public void downloadTemplateFile(HttpServletResponse response) throws IOException {
        StudentInfoInsertUtils.getTemplateFile(response);
    }

    /**
     * 学生信息导出
     * @param response
     * @param query
     * @throws IOException
     */
    @GetMapping("/exportData")
    public void exportStudentInfoData(HttpServletResponse response,  StudentInfoQuery query) throws IOException {
        List<Student> students = studentInfoService.getStudentByQueryInfo(query);
        StudentInfoInsertUtils.getStudentInfoFile(response, students, "学生信息导出文件");
    }

    /**
     * excel 上传导入
     * @param files
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/studentInfo/fileUpload")
    public RespBean uploadStudentInfoFile(@RequestParam("files")  MultipartFile[] files, HttpServletRequest request) throws Exception {
        //校验数据
        List<StudentExcelData> errorList = studentInfoService.saveStudentExcelData(files);
        //如果有错误的数据，就添加到错误的集合中，返回error，并提示
        if (errorList.size() > 0) {
            HttpSession session = request.getSession();
            session.setAttribute("errorList", new ArrayList<>(errorList));
            studentInfoService.getErrorList().clear();
            return new RespBean(ResponseCode.ERROR, errorList.size());
        }
        return new RespBean(ResponseCode.SUCCESS, errorList.size());
    }




}
