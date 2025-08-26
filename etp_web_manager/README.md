## 开发
# etp_web_manager

基于vue的，培训考试系统的管理端 - 前端
```bash
# 克隆项目
git clone https://gitee.com/y_project/RuoYi-Vue

# 进入项目目录
cd ruoyi-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

```
├── smart-ui/views         // 前端功能模块
│       └── app                       // app 应用管理
│       └── category                      // 分类树
│       └── components                    //组件
│       └── dashboard                     // 公共组件-图表
│       └── etp                           // 培训相关功能模块
│           └── banner                         // 轮播页面管理
│           └── category                        // 分类树（老）
│           └── certificate                     // 企业证书
│           └── certificateTemplate             // 证书管理
│           └── certRecord                     // 电子证书
│           └── courseProgress                 // 
│           └── courseware                     // 课件管理
│           └── coursewareBrowsing             // 
│           └── coursewareresource             // 知识库课件管理
│           └── curriculum                     // 课程管理
│           └── curriculumresource             // 知识库课程管理
│           └── dataAnalysis                   // 统计分析
│           └── examination                    // 考试管理（老）
│           └── knowledge                      // 知识点管理
│           └── opinion                        // 意见管理
│           └── questionnaire                  // 问卷
│           └── sign                           // 活动管理
│           └── subject                        // 题目管理（老）
│           └── subjectResource                // 知识库题目管理
│           └── task                           // 任务管理
│           └── testPaper                      // 试卷管理（老）
│           └── testPaperResource              // 知识库试卷管理
│       └── exam                            // 我的考试
│       └── monitor                         // 
│       └── paper                           // 试卷管理
│       └── question                        // 题目管理
│       └── system                          // 系统管理
│       └── tool                            // 代码生成
│       └── train                           // 
│


```