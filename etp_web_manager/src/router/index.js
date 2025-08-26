import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import ParentView from '@/components/ParentView';
import InnerLink from '@/layout/components/InnerLink'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: (resolve) => require(['@/views/system/user/authRole'], resolve),
        name: 'AuthRole',
        meta: { title: '分配角色'}
      }
    ]
  },
  {
    path: '/auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: (resolve) => require(['@/views/system/role/authUser'], resolve),
        name: 'AuthUser',
        meta: { title: '分配用户'}
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'testpaper/paper/:paperId(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/testpaper/paper'], resolve),
        name: 'paper',
        beforeEnter: (to, from, next) => {
          if (to.params.paperId) {
            to.meta.title = '编辑试卷'
          } else {
            to.meta.title = '新增试卷'
            to.meta.activeMenu= '/etp/testpaper'
          }
          next()
        },
      }
    ]
  },

  /** 组织课程 遗弃 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculum/organizeCurriculum/:cid(\\d+)',
        component: (resolve) => require(['@/views/etp/curriculum/organizeCurriculum'], resolve),
        name: 'organizeCurriculum',
        meta: { title: '组织课程' }
      }
    ]
  },

  /** 新增或者编辑课程 :Cid(\d+)*/
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculum/addOrUpdateCurriculum/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/curriculum/addOrUpdateCurriculum'], resolve),
        name: 'addOrUpdateCurriculum',
        beforeEnter: (to, from, next) => {
          if (to.params.id) {
            to.meta.title = '编辑课程'
          } else {
            to.meta.title = '新增课程'
          }
          next()
        },
      }
    ]
  },
  /** 首页进入增加课件页面 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'courseware/components/addOrUpdateCourseware/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/courseware/components/addOrUpdateCourseware'], resolve),
        name: 'addOrUpdateCourseware',
        beforeEnter: (to, from, next) => {
          if (to.params.id) {
            to.meta.title = '编辑课件'
          } else {
            to.meta.title = '新增课件'
          }
          next()
        },
      }
    ]
  },
  /** 首页进入试题增加页面*/
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'subject/components/addOrUpdateSubject/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/subject/components/addOrUpdateSubject'], resolve),
        name: 'addOrUpdateSubject',
        beforeEnter: (to, from, next) => {
          if (to.params.id) {
            to.meta.title = '编辑试题'
          } else {
            to.meta.title = '新增试题'
          }
          next()
        },
      }
    ]
  },
  /** 新增或者编辑任务 :tid(\d+)*/
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'task/addOrUpdateTask/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/task/components/addOrUpdateTask'], resolve),
        name: 'addOrUpdateTask',
        beforeEnter: (to, from, next) => {
          if (to.params.id) {
            to.meta.title = '编辑任务'
          } else {
            to.meta.title = '新增任务'
          }
          next()
        },
        // meta: { title: '编辑任务' }
      }
    ]
  },
  /** 预览课程 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'task/previewCurriculum/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/task/components/previewCurriculum'], resolve),
        name: 'previewCurriculum',
        meta: { title: '课程预览' }
      }
    ]
  },
  /** 预览考试 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'task/previewExamination/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/task/components/previewExamation'], resolve),
        name: 'previewExamination',
        meta: { title: '考试预览' }
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'examination/editExamination/',
        component: (resolve) => require(['@/views/etp/examination/editExamination'], resolve),
        name: 'examination',
        beforeEnter: (to, from, next) => {
          if (to.query.id) {
            to.meta.title = '编辑考试'
          } else {
            to.meta.title = '新增考试'
          }
          next()
        },
      }
    ]
  },
  /**问卷调查*/
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'questionnaire/addOrUpdateQuestionnaire/',
        component: (resolve) => require(['@/views/etp/questionnaire/components/addOrUpdateQuestionnaire'], resolve),
        name: 'addOrUpdateQuestionnaire',
        beforeEnter: (to, from, next) => {
          if (to.query.id) {
            to.meta.title = '编辑问卷'
          } else {
            to.meta.title = '新增问卷'
          }
          next()
        },
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'questionnaire/previewQuestionnaire/',
        component: (resolve) => require(['@/views/etp/questionnaire/components/previewQuestionnaire'], resolve),
        name: 'previewQuestionnaire',
        meta: { title: '调查问卷预览' }
      }
    ]
  },
  /**课程资源详情 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculumresource/info/',
        component: (resolve) => require(['@/views/etp/curriculumresource/info'], resolve),
        name: 'curriculumResourceInfo',
        meta: { title: '课程详情' }
      }
    ]
  },
  /**课程资源列表 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculumresource/index/',
        component: (resolve) => require(['@/views/etp/curriculumresource/index'], resolve),
        name: 'Curriculumresource',
        meta: { title: '课程详情' }
      }
    ]
  },

  /**配置管理 - banner */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'banner/index/',
        component: (resolve) => require(['@/views/etp/banner/index'], resolve),
        name: 'banner',
        meta: { title: '配置轮播' }
      }
    ]
  },
  /**配置管理 - banner - mobile */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'banner/components/editMobileImages/',
        component: (resolve) => require(['@/views/etp/banner/components/editMobileImages'], resolve),
        name: 'banner',
        meta: { title: '配置手机端轮播' }
      }
    ]
  },
  /**配置管理 - banner - PC */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'banner/components/editPCImages/',
        component: (resolve) => require(['@/views/etp/banner/components/editPCImages'], resolve),
        name: 'banner',
        meta: { title: '配置电脑端轮播' }
      }
    ]
  },
  /** 预览活动报名 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'sign/components/signDetail/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/sign/components/signDetail'], resolve),
        name: 'signDetail',
        meta: { title: '活动报名预览' }
      }
    ]
  },
  /** 预览试卷 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'examination/previewTestPaper/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/examination/previewTestPaper'], resolve),
        name: 'previewTestPaper',
        meta: { title: '试卷预览' }
      }
    ]
  },

  /** 判卷页面 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'examination/paperInfo/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/examination/paperInfo'], resolve),
        name: 'paperInfo',
        meta: { title: '判卷' }
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculum/previewCurriculum/',
        component: (resolve) => require(['@/views/etp/curriculum/components/previewCurriculum'], resolve),
        name: 'previewCurriculum',
        meta: {title: '课程预览',noCache:true}
      }
    ]
  },
  /** 预览试卷 */
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'examination/previewTestPaper/:id(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/examination/previewTestPaper'], resolve),
        name: 'previewTestPaper',
        meta: { title: '试卷预览' }
      }
    ]
  },

  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/task/previewTask/',
        component: (resolve) => require(['@/views/etp/task/components/previewTask'], resolve),
        name: 'previewTask',
        meta: {title: '任务预览'}
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculum/analyse/',
        component: (resolve) => require(['@/views/etp/curriculum/analyse'], resolve),
        name: 'analyse',
        meta: {title: '课程分析',noCache:true}
      }
    ]
  },

  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'examinationScoreAnalysis/AnalysisInfo/',
        component: (resolve) => require(['@/views/etp/examinationScoreAnalysis/AnalysisInfo'], resolve),
        name: 'examinationScoreAnalysis',
        meta: {title: '考试分析'}
      }
    ]
  },

  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'questionnaire/components/detail',
        component: (resolve) => require(['@/views/etp/questionnaire/components/questionnaireDetail'], resolve),
        name: 'questionnaireDetail',
        meta: {title: '问卷详情',noCache:true}
      }
    ]
  },
  {
    path: '/etp',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'course/previewCourse/',
        component: (resolve) => require(['@/views/etp/courseware/components/study'], resolve),
        name: 'previewCourse',
        meta: {title: '课件学习',noCache: true}
      }
    ]
  },
  {
    path: '/question-edit',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/',
        component: (resolve) => require(['@/views/question/question/components/editquestion'], resolve),
        name: 'questionEdit',
        meta: { title: '题目编辑' ,activeMenu: '/question/questionInfo',noCache: true}
      }
    ]
  },
  {
    path: '/paper-edit',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/',
        component: (resolve) => require(['@/views/paper/paper/editpaper'], resolve),
        name: 'paperEdit',
        meta: { title: '试卷编辑' ,noCache: true}
      }
    ]
  },
  {
    path: '/paper-view',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'view/',
        component: (resolve) => require(['@/views/paper/paper/viewpaper'], resolve),
        name: 'paperView',
        meta: { title: '试卷预览' ,noCache: true}
      }
    ]
  },


  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'app/appVersion/',
        component: (resolve) => require(['@/views/app/appVersion/index'], resolve),
        name: 'index',
        meta: { title: 'APP版本详情' }
      }
    ]
  },

  {
    path: '/exam-edit',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/',
        component: (resolve) => require(['@/views/exam/exam/components/createOrUpdateExam'], resolve),
        name: 'examEdit',
        meta: { title: '考试编辑'}
      }
    ]
  },

  {
    path: '/record-paper',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'record/',
        component: (resolve) => require(['@/views/exam/exam/components/viewpaper'], resolve),
        name: 'recordView',
        meta: { title: '评卷' ,noCache: true}
      }
    ]
  },
  {
    path: '/train-edit',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/',
        component: (resolve) => require(['@/views/train/train/edittrain'], resolve),
        name: 'trainEdit',
        meta: { title: '习题集编辑' ,activeMenu: '/testpaper/paperInfo',noCache: true}
      }
    ]
  },
  {
    path: '/train-view',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'view/',
        component: (resolve) => require(['@/views/train/train/viewtrain'], resolve),
        name: 'trainView',
        meta: { title: '习题集预览' ,noCache: true}
      }
    ]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  base: '/admin/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
