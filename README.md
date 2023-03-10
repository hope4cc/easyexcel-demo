## 一、创建

### 1、数据库

```plsql
一个student对应一个class班级,一个班级对应多个学生
    student对class 是多对一的关系
    class对student 是一对多的关系

    一个班级class对应一个专业major ,一个专业major有多个班级class
    class对major   是多对一的关系
    major对class   是一对多的悬系

    一个专业major对应一个学院academy,一个学院有多个专业
    major对academy   是多对一的关系
    academy对major   是一对多的关系

 # 学院、专业、班级、学号、身份证号、报道时间（yyyy-MM-dd HH:mm:ss）、照片、状态（在校、辍学）】
select ad.name,
       ma.name,
       cls.name,
       stu.id,stu.name,stu.idCard,stu.createTime,stu.avatar,stu.status
    from
        student stu  join class cls on stu.classId=cls.id
        join major ma on cls.of_major=ma.id
        join academy ad on ma.of_academy=ad.id;

```

> 不使用外键

![输入图片说明](images/1.png)

> 使用外键

![输入图片说明](images/2.png)

### 2、项目搭建Springboot+mybatis

![输入图片说明](images/3.png)

```plsql
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>

        <!-- MyBatis pageHelper -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.5</version>
        </dependency>

        <!-- aop -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <!-- easyExcel -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>easyexcel</artifactId>
            <version>3.1.1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.12.0</version>
        </dependency>

        <!--        JSR303数据校验-->
        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
        </dependency>
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.alibaba.fastjson2/fastjson2 -->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>2.0.12</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/cn.hutool/hutool-all -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.3</version>
        </dependency>
    </dependencies>
```

---

## 二、模板和数据导出

### 1、模板

![输入图片说明](images/4.png)

```java
    /**
     * 模板接口
     * @param response
     * @throws IOException
     */
    @GetMapping("/templateFile/download")
    public void downloadTemplateFile(HttpServletResponse response) throws IOException {
        StudentInfoInsertUtils.getTemplateFile(response);
    }
```

```java
   /**
     * 导出模板
     * @param response
     * @throws IOException
     */
    public static void getTemplateFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode("学生信息模板", "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StudentExcelTemplateData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(EXCEL_DATA);
    }

```

> [http://localhost:9000/templateFile/download](http://localhost:9000/student/exportData)

![输入图片说明](images/5.png)

---

### 2、数据导出

```java
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
```

```java
/**
     * 导出数据
     * @param response
     * @param students
     * @param currFilename
     * @throws IOException
     */
    public static void getStudentInfoFile(HttpServletResponse response, List<Student> students, String currFilename) throws IOException {
        List<StudentExcelData> excelData = convertStudentInfo(students);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode(currFilename, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StudentExcelData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(excelData);
    }
```

```java
 /**
     *  转换 excel list
     * @param students
     * @return
     */
    private static List<StudentExcelData> convertStudentInfo(List<Student> students) {
        List<StudentExcelData> excelData = new ArrayList<>();
        for (Student student : students) {
            excelData.add(new StudentExcelData(student.getId(), student.getName(), student.getNation(), student.getSex(),
                    student.getAge().toString(), student.getPoliticsStatus(), student.getIdCard(), student.getPhoneNum(),
                    student.getEmail(), student.getAcademy().getName(), student.getMajor().getName(), student.getClasses().getName(),
                    student.getCreateTime(),student.getStatus()));
        }
        return excelData;
    }
```

> [http://localhost:9000/student/](http://localhost:9000/student/exportData)exportData

![输入图片说明](images/6.png)

## 三、导入数据

### 1、导入

```java
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
```

```java
/**
     * 错误信息
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
```

```java
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
```

```java
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
```

```java
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
    	//错误消息
        String errorMessage = null;
        try {
            errorMessage = ExcelValidateHelper.validateEntity(studentExcelData);
        } catch (NoSuchFieldException e) {
            errorMessage = "解析数据出错";
            e.printStackTrace();
        }
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

```

### 2、类型正确导入解析

![输入图片说明](images/7.png)
![输入图片说明](images/8.png)

### 2、类型错误导入->提示错误信息

> 把日期改成中文

![输入图片说明](images/9.png)
![输入图片说明](images/10.png)

## 四、问题、解决

### id只能偶数

#### 1、插入线程频率较高，主键相同不能导入

![输入图片说明](images/11.png)
搜索后发现有如下解决办法

> [https://blog.csdn.net/qq_39390545/article/details/91046282](https://blog.csdn.net/qq_39390545/article/details/91046282)
> 原因一：
> [主键](https://so.csdn.net/so/search?q=%E4%B8%BB%E9%94%AE&spm=1001.2101.3001.7020)没有设置自增~
> 原因二：
> 插入线程频率较高，没有处理好事务，造成插入sql执行顺序混乱
> 解决办法
> 加上ignore关键词即可，意思是如果该主键已存在，则不执行该条sql。
> 当然，有同学又问了，那我这条数据岂不是丢失了？
> 当然不，如果没有成功添加该数据，接口返回值就为0（@新增0条数据），然后需要再次调用插入接口对该数据进行二次插入。！！！这里提醒接口返回值用void的同学，返回值最好用int（@返回值是有意义的）。
> 当然不，如果没有成功添加该数据，接口返回值就为0（@新增0条数据），然后需要再次调用插入接口对该数据进行二次插入。！！！这里提醒接口返回值用void的同学，返回值最好用int（@返回值是有意义的）。


#### 2、解决id冲突，但是id还能只能偶数

> debug 发现是集合中有两条一样的数据,但是导入时候数据是不一样的,不能导入,而且id是学生的信息生成的
![输入图片说明](images/12.png)
![输入图片说明](images/13.png)

> **最后发现**
> 手贱!测试的时候list.add写在外面 ,忘记删除了,导致读取了一条数据,但是往list里添加了两条,导致上述id主键相同->而不能导入

## 未解决的问题

- 图片导出导出还没完成，计划数据库中存储url，前端页面下载展示
- 格式错误无法提示到具体的几行几列，但是可以提示那些字段不能为空
- id 也就是学号 唯一可以保证，但是会出现全偶数或者全奇数问题




