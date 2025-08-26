# 培训考试系统服务端

## 本地调试

本地运行项目时请注释掉 ruoy-admin/pom.xml 中的

```
<targetPath>${project.build.directory}/config</targetPath>
```

该项目的配置文件位于 `sinosoft-admin/src/resources/config` 下，分别包含

- dev，开发环境

- test，测试环境

- uat，预发布测试环境

- prod，正式环境

四套配置参数。

当开发人员在本地运行项目时会默认使用 dev 下的配置文件，如果要发布请使用以下命令打包：

```shell
maven clean package -P prod
```

## 数据库密码加密

1、 用以对数据库密码进行加密
java -classpath sinosoft-tools.jar sinosoft.tools.Encrypt 12

## 机器码

2、 用以获取机器码等信息
java -classpath sinosoft-tools.jar sinosoft.tools.AuthCode

将 AuthCode 生成的机器码新建并拷贝至 userAuth.txt 文件，并在第二行添加过期时间(格式 YYYY-MM-DD)

生成授权文件
java -classpath sinosoft-license.jar com.sinosoft.license.tools.CreateAuthCode

将授权文件上传至当前用户的 home 目录下，新建 .auth 目录并将 authCode.txt 拷贝至

## 图片的处理

1. 图片需要在调用 POST 接口保存时，将

保存图片 URL 时，需要以 JSON 形式提交，并将图片对应的 key 填写到配置文件 storage.fields 中，这样在保存到数据库时，URL 会被替换为变量 `{storage.path.url}`

2. 使用 @PreProcessField 注解存在图片的字段
3. 如果返回的是 list，则使用 `return getDataTable(list, SysNotice.class);`


## 模块说明
```
├── etp_server[etp_root]                               // 后端父模块
│       └── bin                                        // 打包和启动脚本
│       └── change                                     //  
│       └── doc                                        // 部分文档 不全
│       └── docker                                     // 容器部署相关文件
│       └── sinosoft-admin                             // 偌依框架管理admin
│       └── sinosoft-common                            // 公共配置模块  工具类
│       └── sinosoft-etp-api                           // 移动端接口
│       └── sinosoft-etp-app                           // app下载、更新
│       └── sinosoft-etp-core                          // 管理端、学员端公共Bean
│       └── sinosoft-etp-cp                            // 培训管理端
│       └── sinosoft-etp-up                            // 培训学员端
│       └── sinosoft-framework                         // 框架模块
│       └── sinosoft-generator                         // 代码生成模块
│       └── sinosoft-quartz                            // 定时任务模块
│       └── sinosoft-system                            // 系统模块
│       └── sql                                        // 
```

## 数据权限改造
```
数据权限控制逻辑：
1、在登录时
    a.根据指定的规则获取租户id(修改登录sql，规则在sql中定义，即顶级部门的直接下级部门id)， 用于控制租户权限
    b、获取部门对于得的ancestors字段，部门祖级，用于在权限控制的注解实现类中组装权限sql语句时进行like应用，用于上下级部门权限
2、在各业务资源表、类别表等需要进行数据权限控制的表中增加如下字段，用于权限控制
    创建资源的用户：create_user_id
    创建资源的用户所属部门：department_id
    创建资源的用户所属租户：tenant_id
    资源是否公开：pub_flag                    这个需要修改前端，看具体业务是否需要。默认为不公开
    由于原来有点表中有user_id字段和dept_id，并且set方法返回的不是void，大概是想实现链式语法，所以这里改用create_user_id,department_id字段名
3、在新增时，给四个字段进行赋值，暂不支持修改时更改四个字段的值
4、在查询方法增加类似 @EtpDataScope(alias = "t")  这样的注解，其中alias为资源表的别名，即上述增加的四个字段所处的表
   在查询方法对应的sql语句中，增加数据权限控制
5、具体的控制方式在com.sinosoft.framework.aspectj.EtpDataScopeAspect中
   通过在参数定义启用哪种规则 10001  ETP资源数据权限控制方式   etp.datascope.mode
   规则如下(0租户等同于不属于任何一个租户)：   
    1： 全部
    2： 自定义(未实现)
    30：仅本部门
    31：本部门及所有下属部门
    32：本部门及所有上级部门
    33：本部门及所有上下级部门
    5： 仅本人
    60：仅本租户不含0租户
    61：本租户及0租户
    
 
 存在的问题：
 1、发布资源时，可以设置范围，可能和这个逻辑存在冲突，目前还没有好的方案解决，设置范围对应的查询语句比较复杂，后续再完善
 2、直接采用sql语句或mapper中参数不是实体类时，需要手工额外处理

API接口列表：
│   └── bin 
│        └──get  查询标签列表                                      /etp/tagWithCp/relateTag?modelType=KJ
│        └──get  查询课程标签(KC为课程)                        不改  /etp/tagWithCp/relateTag?modelType=KC    
│        └──get  查询课件标签(KJ为课件)                        不改  /etp/tagWithCp/relateTag?modelType=KJ    
│        └──get  获取参数(后缀为参数key)                       不改  /system/config/configKey/cp.qrcg.url
│
│────── 标签管理 标签表：t_tag    关联表：t_******_tag   ******代表具体业务资源，下面描述括号中为关联表中用来表示业务资源id的字段名
│               t_questionnaire_tag(wjid) 
│               t_sign_tag(hdid)
│               t_task_tag(rwid)
│               t_curriculum_tag(kcid)    课程
│               t_subject_tag(tkid)       题库(其实是题)  已不用   用question 题目信息表  题目没有标签 只有分类即题库  t_subject应该也不用了
│               t_courseware_tag(kjid)    课件
│               t_testpaper_tag(sjid)     试卷 paper试卷基础信息表
│               t_examination_tag(ksid)   考试
│               t_certificate_tag(zsid)   证书
│────── 分类管理  资源分类表 ******_category 关联表：******_inter_category   原来t_category表已不用
│        sinosoft-etp-api:com.sinosoft.admin.category.core.CategoryType
│        // 资源(课件 课程等)分类表名  
│        map1.put(CategoryEnum.QUESTION_CATEGORY, "question_category");
│        map1.put(CategoryEnum.PAPER_CATEGORY, "paper_category");
│        map1.put(CategoryEnum.EXAM_CATEGORY, "exam_category");
│        map1.put(CategoryEnum.COURSEWARE_CATEGORY, "courseware_category");
│        map1.put(CategoryEnum.CURRICULUM_CATEGORY, "curriculum_category");
│        map1.put(CategoryEnum.KNOWLEDGE_CATEGORY, "knowledge_category");
│        map1.put(CategoryEnum.TRAIN_CATEGORY, "train_category");
│        // 资源(课件 课程等)和资源分类进行关联的表名│
│       map2.put(CategoryEnum.QUESTION_CATEGORY, "question_inter_category");
│        map2.put(CategoryEnum.PAPER_CATEGORY, "paper_inter_category");
│        map2.put(CategoryEnum.EXAM_CATEGORY, "exam_inter_category");
│        map2.put(CategoryEnum.COURSEWARE_CATEGORY, "courseware_inter_category");
│        map2.put(CategoryEnum.CURRICULUM_CATEGORY, "curriculum_inter_category");
│        map2.put(CategoryEnum.KNOWLEDGE_CATEGORY, "knowledge_inter_category");
│        map2.put(CategoryEnum.TRAIN_CATEGORY, "train_inter_category");
│
│────── 资源类别管理 API即url各自模块，但具体实现都在com.sinosoft.admin.category.service.impl.CategoryServiceImpl
│        └──资源类别表
│             └──t_category                作废   有部分语句还引用了此表，属bug，如课件、课程
│             └──category                  作废 
│             └──******_category           某资源得分类表
│             └──******_inter_category     某资源分类和资源分类关联的表
│        └──get  查询****类别树                               已改  /admin/v1/****/category/treeselect
                 com.sinosoft.admin.category.service.impl.select方法 
                  加注解@EtpDataScope(index = 1)
                  String sql 增加where条件拼接
│        └──post 新增****类别                                 已改  /admin/v1/****/category                    put修改 delete删除 
│                com.sinosoft.admin.category.service.impl.insertColumns变量
                    增加插入字段
                 com.sinosoft.admin.category.service.impl.insert方法
                       增加EtpDataScopeUtils.setDataScope(category) 
                       sqlBuilder.append增加新字段处理                                              
│
│────── ***课件管理*****************************
│────── etp: sinosoft-etp-cp  admin:sinosoft-etp-api
│────── 课件管理 课件表：t_courseware 课件类别表：t_courseware_category 课件分类关联表：courseware_inter_category     原来用t_category表，代码中还有bug，没改全，课程类似
│        └──get  查询课件类别树                               已改  /admin/v1/courseware/category/treeselect
│        └──post 新增课件类别                                 已改  /admin/v1/courseware/category                put修改 delete删除 
│        └──get  查询课件列表                                 已改  /etp/coursewareWithCp/list                      
│        └──post 新增课件信息                                 已改  /etp/coursewareWithCp                        put修改 delete删除 
│
│────── ***课程管理*****************************
│────── etp: sinosoft-etp-cp  admin:sinosoft-etp-api
│────── 课程管理 课程表：t_courseware 课件类别表：t_courseware_category 课件分类关联表：courseware_inter_category
│        └──get  查询课程类别树                                已改    /admin/v1/curriculum/category/treeselect
│        └──post 新增课程类别                                  已改    /admin/v1/curriculum/category              put修改 delete删除 
│        └──get  查询课程列表                                  已改    /etp/curriculumWithCp/list   
│        └──post 新增课程信息                                  已改    /etp/curriculumWithCp                      put修改 delete删除  
│
│────── ***学习任务管理*****************************
│────── sinosoft-etp-cp
│────── 学习任务管理  表：t_task    无分类
│        └──post 新增任务(下一步或暂存)                          已改    /etp/taskWithCp 
│        └──post 新增任务,第二步提交                             不改    /etp/taskWithCp/completeTask
│        └──get  查询课程列表                                   已改    /etp/taskWithCp/list
│
│────── ***知识点管理*****************************
│────── sinosoft-etp-api
│────── 知识点管理  表：knowledge 分类表：knowledge_category 分类关联表：knowledge_inter_category
│        └──控制器：KnowledgeCategoryController
│        └──get  查询知识点类别树                                已改    /admin/v1/knowledge/knowledgeCategory/treeSelect
│        └──post 新增知识点类别                                  已改    /admin/v1/knowledge/category              put修改 delete删除 
│
│        └──控制器：KnowledgeController
│        └──get  查询知识点列表                                  已改    /admin/v1/knowledge/knowledge/list
│        └──post 新增知识点信息                                  已改    /admin/v1/knowledge/knowledge              put修改 delete删除  
│
│────── ***题库管理***************************** 
│────── sinosoft-etp-api
│────── 题库管理(这里题库其实就是题目表，题库实际是类别表)  表：question 分类表：question_category 分类关联表：question_inter_category
│        └──控制器：QuestionCategoryController
│        └──get  题库即类别树(题库即类别)                         已改    /admin/v1/question/category/treeselect
│        └──get  新增题库 即类别                                已改    /admin/v1/question/category
│
│        └──控制器：QuestionController
│        └──get  查询题目列表@EtpDataScope(alias = "qu")         已改    /admin/v1/question/list
│        └──post 新增题目信息(不是把QuestionReqDTO作为插入对象)      已改    /admin/v1/question                         put修改 delete删除  
│        └──QuestionReqQuery 查询实体类未继承 BaseEntity ，因此需要手工添加4个字段及
│        ******************** 还有很多其他方法，需要仔细研究代码
│        └──get  随机取题                                       已改    /admin/v1/question/random
│        └──get  按题目难度统计题目数量                  未找到调用得地方    /admin/v1/question//fact/level                                     
│
│────── ***考试管理***************************** 
│────── sinosoft-etp-api
│────── 考试管理  表：exam  分类表：exam_category 分类关联表：exam_inter_category
│        └──控制器：ExamCategoryController
│        └──get  查询考试类别树                                   已改    /admin/v1/exam/category/treeselect
│        └──post 新增考试类别                                     已改    /admin/v1/exam/category
│
│        └──控制器：QuestionController│
│        └──get  查询考试列表@EtpDataScope(alias = "ex")          已改    /admin/v1/exam/list
│        └──post 新增考试信息(不是把ExamReqDTO作为插入对象)           已改    /admin/v1/exam                         put修改 delete删除  
│        └──ExamQuery 查询实体类未继承 BaseEntity ，因此需要手工添加4个字段及
│
│────── ***试卷管理***************************** 
│────── 试卷管理  表：paper    分类表：paper_category 分类关联表：paper_inter_category
│        └──控制器：PaperCategoryController
│        └──get  查询试卷类别树                                    已改    /admin/v1/paper/category/treeselect
│        └──get  新增试卷类别                                      已改    /admin/v1/paper/category
│
│        └──控制器：PaperController
│        └──get  查询试卷列表@EtpDataScope(alias = "p")            已改    /admin/v1/paper/list
│        └──post 新增试卷信息(不是把PaperReqDTO作为插入对象)           已改    /admin/v1/paper                        put修改 delete删除 
│        └──PaperQuery 查询实体类未继承 BaseEntity ，因此需要手工添加4个字段及
│
│────── ***通知公告管理***************************** 
│────── sinosoft-system
│────── 通知公告管理 表：sys_notice 此为框架系统表
│────── 为了不过多得引入模块依赖，EtpDataScopeUtils工具类也移植到了system模块中
│        └──控制器：SysNoticeController
│        └──get  查询通知公告列表@EtpDataScope(alias = "sn")          已改    /system/notice/list
│        └──post 新增通知公告信息                                     已改    /system/notice
│ 
│ 
│────── ***活动管理***************************** 
│────── sinosoft-etp-cp
│────── 活动管理  表：t_sign
│        └──控制器：TSignWithCpController
│        └──get  查询活动列表@EtpDataScope(alias = "ts")             已改     /etp/signWithCp/list
│        └──post 新增活动                                           已改     /etp/signWithCp
│ 
│ 
│────── ***问卷管理***************************** 
│────── sinosoft-etp-cp
│────── 问卷管理 表：t_questionnaire
│        └──控制器：TQuestionnaireWithCpController
│        └──get  查询问卷列表@EtpDataScope(alias = "tq")             已改     /etp/questionnaireWithCp/list
│        └──post 新增问卷调查首次保存(暂存、下一步)                       已改    /etp/questionnaireWithCp
│        └──put  下一步、非首次暂存、发布                               不改     /etp/questionnaireWithCp   
│
│
│────── ***意见管理***************************** 
│────── sinosoft-etp-cp
│────── 意见管理 表：t_opinion
│        └──控制器：TOpinionWithCpController
│        └──get  查询意见建议列表@EtpDataScope(alias = "o")           已改       /etp/opinionWithCp/list
│        └──post 新增意见建议                                        已改       /etp/opinionWithCp
│
│
│────── ***证书管理***************************** 
│────── etp： sinosoft-etp-cp  api: sinosoft-etp-api

│────── 证书管理
│────── 1、电子证书颁发记录 表：certificate_grant_record
│        └──控制器：CertificateGrantRecordController 
│        └──get  电子证书颁发记录@EtpDataScope(alias = "r")            已改     /api/v1/record/list
│        └──post 新增颁发记录                                         已改     /api/v1/record
│────── 2、证书模板管理(菜单：证书管理)  表：certificate_template
│        └──控制器：CertificateTemplateController
│        └──get  获取模板列表@EtpDataScope                            已改     /api/v1/certificate/list
│        └──post 新增模板                                            已改     /api/v1/certificate
│────── 3、企业证书  表：t_certificate  别名：tc
│        └──控制器：TCertificateWithCpController 
│        └──get  获取模板列表@EtpDataScope(alias = "tc")              已改     /etp/certificateWithCp/list
│        └──post 新增证书                                            已改     /etp/certificateWithCp


│────── 统计分析
│────── 1、考试成绩分析 列表  表：t_examination  似乎不对 这个表没用到
│        └──控制器：TExaminationWithCpController   TExaminationWithCpMapper.xml->selectTExaminationList
│        └──get  考试成绩分析 列表/etp/examinationWithCp/list

│
│
│
 

│────── ***代码模板***************************** 
│──────代码修改模板及说明，方便拷贝：
│────── 服务层列表查询方法注解
    @DataScope(deptAlias = "d") //错误用法 别名错误
    @EtpDataScope(alias = "t")
│────── 服务层新增方法增加
    EtpDataScopeUtils.setDataScope(tTask);// 20250520 lyd etp数据权限相关字段
│────── 列表查询数据权限sql where子句
            <!-- 数据权限控制 -->
            <!--
           <if test="params.dataScope != null and params.dataScope != ''">${params.dataScope}</if>
            -->
            <if test="params.etpDataScope != null and params.etpDataScope != ''">${params.etpDataScope}</if>
            
            
            如果原来没有老的数据权限控制即dataScope，则拷贝下面代码
            <!-- 数据权限控制 -->
            <if test="params.etpDataScope != null and params.etpDataScope != ''">${params.etpDataScope}</if>

│──────  特殊权限，如何取舍 并集还是交集的问题
           └──第一种用法，将实体对象传入mapper
                <!-- 数据权限控制 todo:和上面的规则如何取舍？-->
                <if test="params.etpDataScope != null and params.etpDataScope != ''">
                    or (1=1 ${params.etpDataScope})
                </if>
           └──第二种用法，将最终解析的数据权限sql作为参数传入mapper
                <!-- 数据权限控制 todo:和上面的规则如何取舍？-->
                <if test="etpDataScope != null and etpDataScope != ''">
                    or (1=1 ${etpDataScope})
                </if>
                
                

│────── 新增插入数据权限相关字段sql insert子句
            /*数据权限字段*/
            <if test="createUserId != null">create_user_id,</if>
            <if test="departmentId != null">department_id,</if>
            <if test="tenantId != null">tenant_id,</if>
            <if test="pubFlag != null">pub_flag,</if>

│────── 新增插入数据权限相关字段sql values子句
            /*数据权限字段*/
            <if test="createUserId != null">#{createUserId},</if>
            <if test="departmentId != null">#{departmentId},</if>
            <if test="tenantId != null">#{tenantId},</if>
            <if test="pubFlag != null">#{pubFlag},</if>

│────── 类别bug修复 
        /*tg.name as categoryName,*/
        GROUP_CONCAT(DISTINCT cat.name SEPARATOR ',') categoryName,
               
               
        <!--left join t_category tg on tg.id = t.categoryId
        fix:类别bug-->
        LEFT JOIN curriculum_inter_category tc on tc.obj_id = t.id
        LEFT JOIN curriculum_category cat on cat.cat_id = tc.cat_id            


│────── 查询实体类未继承 BaseEntity ，因此需要手工添加4个字段及    
        // 数据权限相关 lyd 20250517  create_user_id  tenant_id department_id  pub_flag
        private Map<String, Object> params;
        public Map<String, Object> getParams() {
            if (params == null) {
                params = new HashMap<>();
            }
            return params;
        }
    
        public void setParams(Map<String, Object> params)
        {
            this.params = params;
        }
        //dept_id 不能用，而用 department_id，是因为有的表已经有这个字段，而且public TOpinionWithCp setDeptId(Long deptId)  返回的不是void
        //user_id 不能用，而用create_user_id 原因同上
        public Long createUserId;
        public Long getCreateUserId() {
            return createUserId;
        }
        public void setCreateUserId(Long createUserId) {
            this.createUserId = createUserId;
        }
        public Long departmentId;
        public Long getDepartmentId() {
            return departmentId;
        }
        public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }
        public String tenantId;
        public String getTenantId() {
            return tenantId;
        }
        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }
        private String pubFlag;
        public String getPubFlag() {
            return pubFlag;
        }
        public void setPubFlag(String pubFlag) {
            this.pubFlag = pubFlag;
        }            

```


## 学员端API数据权限改造
```
│────── ***首页***************************** 
│────── 数量统计  
│        └──控制器：HomeController
│        └──课程统计：未完成课程、已领取课程 表：t_curriculum_student    个人数据(学员id xyid)  不改  get  /etp/home/kctjNum             
│        └──考试统计：将结束考试、将开始考试                                                 已改  get  /etp/home/kstjNum
                    1、不是以BaseEntity作为参数  --增加了一个BaseEntity参数用来接数据权限sql 到mapper层传sql字符串，因为xml不是接收的实体类而是具体的参数
                       改造EtpDataScopeUtils,直接通过getDataScopeSql方法获取数据权限sql
                    2、数据权限控制方式，业务具体怎么定？？？目前放到了外围，即 发布范围和系统数据权限的交集
                    UserExamServiceImpl.getCountNum
                    UserExamMapper.selectCountNum                    
│        └──任务统计：将结束任务、未完成任务                            个人数据(学员id xyid)  不改 get  /etp/home/rwtjNum
│        └──证书统计：已获得证书                                      个人数据(学员id xyid)  不改 get  /etp/home/zstjNum

│─────────获取推荐课程(其实是推荐课程，和知识点应该没关系？？？)    推荐课程    后面的更多
            API地址:     sinosoft-etp-up TCurriculumController    已改   get        /etp/curriculum/getKnowledgeInfo  
            方法链:      TCurriculumServiceImpl.getKnowledgeInfo      TCurriculumMapper.getKnowledgeInfo
            关键sql      t_curriculum t  LEFT JOIN  t_curriculum_student s
│─────────查询curriculum列表   我的课程  后面的 最近学习 最近领取 更多
            API地址:     sinosoft-etp-up  TCurriculumController  个人数据(学员id xyid) 不改 get /etp/curriculum/list

│─────────查询考试列表 我的考试 UserExamReqQuery不是继承BaseEntity    和 /etp/home/kstjNum在一个xml文件中
            get    /user/v1/exam/list   我的考试      后面的更多

        //代码方式设置数据权限  比较特殊，学生端，从代码级修改
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserExamServiceImpl   getExams(注解方式) getExamInfo(代码方式) getComingEnd(代码方式) checkAbleToStartTheExam(代码方式) makeupUserExam(代码方式)  checkExamRecordIng(代码方式 private)
        UserExamMapper 一共四个select语句：selectUserExamList  selectUserExamInfo selectComingEndExam selectCountNum


│─────────我的考试： sinosoft-etp-api: UserExamController                                已改   get    /user/v1/exam/list     如上描述
│─────────我的任务:  sinosoft-etp-up： TTaskController               个人数据(学员id xyid)  不改   get    /etp/task/list
│─────────问卷调查： sinosoft-etp-up： TQuestionnaireController                            已改   get    /etp/questionnaire/list    首页不显示(我的课程--》右侧更多-->问卷调查)
│─────────查询轮播图列表 sinosoft-etp-up：TBannerController                        公共资源  不改   get    /etp/banner/list****
│─────────上次学习(课程统计中的)： sinosoft-etp-up：HomeController       个人数据(学员id xyid) 不改   get    /etp/home/lastStudy
│─────────即将结束的考试信息(考试统计中的) sinosoft-etp-up：HomeController         上面统一修改  已改   get    /etp/home/comingEndExam 
 
 

│────── ***更多进去后的页面***************************** 
│        └──我的课程
│        └──我的考试
│        └──考试记录：sinosoft-etp-api: UserExamController                                 已改   get    /user/v1/exam/history
│            插入不加字段，依据考试的权限来定  UserExamRecordReqQuery 不是继承BaseEntity 查询加字段
│        └──我的任务          
│        └──我的证书：sinosoft-etp-api: CertificateGrantRecordController                     和管理端重叠  已改  get    /api/v1/record/certList  
│        └──问卷调查

个人信息修改   不改 /system/user/profile 

│────── ***APP-手机端特有API***************************** 
首页推荐课程 sftj 1推荐 0不推荐  sinosoft-etp-up：TCurriculumController   不改           get    /etp/curriculum/tjList     同  /etp/curriculum/list 
领取课程                       sinosoft-etp-up：TTCurriculumController 个人数据，不改    post   /etp/curriculum/receiveCurriculum
获取首页推荐课程                 sinosoft-etp-up：HomeController         个人数据，不改    get    /etp/home/getHomeCurriculum
获取课程目录列表                 sinosoft-etp-up：TTCurriculumController   单条数据，不改  get     /etp/curriculum/selectTCurriculumByIds
获取课程标签                                                         标签暂不改          get     /etp/curriculum/getCurriculumTags
获取用户答题信息                 sinosoft-etp-up：HomeController       就提试题得答案，不改 get    /etp/testpaperSubject/selectAnswerInfo
获取任务和考试数量                sinosoft-etp-up：HomeController        个人数据，不改     get    /etp/home/getTaskAndExamNum
获取轮播图                                                            web学员端 已改      get    /etp/banner/list
获取首页问卷                      sinosoft-etp-up：HomeController        个人数据，不改     get    /etp/home/getHomeQuestionnaire
获取首页报名活动                   sinosoft-etp-up：HomeController        个人数据，不改     get    /etp/home/getHomeActivite
获取报名列表                      sinosoft-etp-up：TSignController                   已改  get    /etp/sign/list  
立即报名                         sinosoft-etp-up：TSignController                   已改   post   /etp/sign
意见反馈                         sinosoft-etp-up：TOpinionController                已改   post   /etp/opinion
获取意见反馈                      sinosoft-etp-up：TOpinionController        个人数据，不改   get    /etp/opinion/list
错题列表   此模块暂未考虑数据权限    sinosoft-etp-up：TErrorbookController              不改    get    /etp/errorbook/list
课程保存习题   不改，t_subject表已经不用了   /etp/curriculum/addSubject











```
