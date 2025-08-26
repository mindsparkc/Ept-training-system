## 开发
# etp_web_student

基于vue的培训考试系统的学生端，Web前端
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
│       └── components                    //组件
│       └── dashboard                     // 公共组件-图表
│       └── etp                           // 培训相关功能模块
│           └── certRecord                     // 我的证书
│           └── curriculum                     // 我的课程
│           └── errorbook                      // 错题本
│           └── exam                           // 我的考试（老）
│           └── historyexam                    // 历史考试（老）
│           └── qrcg                           // 二维码生成
│           └── questionnaire                  // 问卷
│           └── task                           // 任务管理
│           └── wqb                           // 
│       └── exam                              // 我的考试
│       └── home                              // 
│       └── monitor                           // 试卷管理
│       └── opinion                          // 意见反馈
│       └── sign                             // 活动管理
│       └── system                           //系统管理
│       └── tool                             // 代码生成
│