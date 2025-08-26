import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// 解决报错
const originalPush = Router.prototype.push
const originalReplace = Router.prototype.replace
// push
Router.prototype.push = function push (location, onResolve, onReject) {
  console.log(onReject + onResolve)
  // if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => {
    console.log(err)
    originalPush.call(this, location, onResolve, onReject)
  });
}
// replace
Router.prototype.replace = function push (location, onResolve, onReject) {
  console.log(onReject + onResolve)
  // if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
  return originalReplace.call(this, location).catch(err => {
    console.log(err)
    originalPush.call(this, location, onResolve, onReject)
  })
}


/* Layout */
import Layout from '@/layout'
import ParentView from '@/components/ParentView';
import InnerLink from '@/layout/components/InnerLink'
import PersonCenter from "@/layout/PersonCenter";


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
  // {
  //   path: '',
  //   component: Layout,
  //   redirect: 'index',
  //   children: [
  //     {
  //       path: 'index',
  //       component: (resolve) => require(['@/views/index'], resolve),
  //       name: '首页',
  //       meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
  //     }
  //   ]
  // },

  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/home/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '',
    component: PersonCenter,
    redirect: 'historyExam',
    children: [
      {
        path: 'historyExam',
        component: (resolve) => require(['@/views/etp/historyexam/index'], resolve),
        name: '考试记录（旧）',
        meta: { title: '考试记录（旧）', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '',
    component: PersonCenter,
    redirect: 'examManagement',
    children: [
      {
        path: 'examManagement',
        component: (resolve) => require(['@/views/etp/exam/index'], resolve),
        name: '我的考试（旧）',
        meta: { title: '我的考试（旧）', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },

  {
    path: '',
    component: PersonCenter,
    redirect: 'examInfo',
    children: [
      {
        path: 'examInfo',
        component: (resolve) => require(['@/views/exam/exam/index'], resolve),
        name: '我的考试',
        meta: { title: '我的考试', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },

  {
    path: '',
    component: PersonCenter,
    redirect: 'curriculum',
    children: [
      {
        path: 'curriculum',
        component: (resolve) => require(['@/views/etp/curriculum/index'], resolve),
        name: '我的课程',
        meta: { title: '我的课程', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '',
    component: PersonCenter,
    redirect: 'stuCert',
    children: [
      {
        path: 'stuCert',
        component: (resolve) => require(['@/views/etp/certRecord/index'], resolve),
        name: '我的证书',
        meta: { title: '我的证书', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '',
    component: PersonCenter,
    redirect: 'task',
    children: [
      {
        path: 'task',
        component: (resolve) => require(['@/views/etp/task/index'], resolve),
        name: '我的任务',
        meta: { title: '我的任务', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: PersonCenter,
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
    path: '/taskManagement',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'detail/',
        component: (resolve) => require(['@/views/etp/task/components/detail'], resolve),
        name: 'taskDetail',
        meta: { title: '任务详情', icon: '' }
      }
    ]
  },
  {
    path: '/examManagement',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'detail/:examId(\\d+)',
        component: (resolve) => require(['@/views/etp/exam/components/detail'], resolve),
        name: 'examDetail',
        meta: { title: '考试详情', icon: '' }
      }
    ]
  },
  {
    path: '/etp',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'curriculum/previewCurriculum/',
        component: (resolve) => require(['@/views/etp/curriculum/components/previewCurriculum'], resolve),
        name: 'previewCurriculum',
        meta: {title: '课程学习',noCache: true}
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'questionnaire/answer/',
        component: (resolve) => require(['@/views/etp/questionnaire/components/answer'], resolve),
        name: 'answer',
        meta: {title: '问卷答题'}
      }
    ]
  },
  {
    path: '',
    component: PersonCenter,
    redirect: 'questionnaire',
    children: [
      {
        path: 'questionnaire',
        component: (resolve) => require(['@/views/etp/questionnaire/index'], resolve),
        name: '调查问卷',
        meta: { title: '调查问卷', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/etp',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'questionnaire/detail/',
        component: (resolve) => require(['@/views/etp/questionnaire/components/detail'], resolve),
        name: 'detail',
        meta: {title: '问卷详情'}
      }
    ]
  },
  {
    path: '/etp',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'historyexam/components/paperInfo/:tid(\\d+|\\d?)',
        component: (resolve) => require(['@/views/etp/historyexam/components/paperInfo'], resolve),
        name: 'paperInfo',
        meta: {title: '历史考试详情'}
      }
    ]
  },
  {
    path: '/etp',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'curriculum/components/curriculumAll/',
        component: (resolve) => require(['@/views/etp/curriculum/components/curriculumAll'], resolve),
        name: 'curriculumAll',
        meta: {title: '全部课程'}
      }
    ]
  },

  {
    path: '/examStart',
    component: ParentView,
    hidden: true,
    children: [
      {
        path: 'detail/:examId(\\d+)',
        component: (resolve) => require(['@/views/exam/exam/components/detail'], resolve),
        name: 'examDetails',
        meta: { title: '开始考试'}
      }
    ]
  },

  {
    path: '/showHistoryExam',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'view/',
        component: (resolve) => require(['@/views/exam/exam/components/viewpaper'], resolve),
        name: 'historyExamView',
        meta: { title: '历史考试详情'}
      }
    ]
  },

  {
    path: '',
    component: PersonCenter,
    redirect: 'examRecord ',
    children: [
      {
        path: 'examRecord',
        component: (resolve) => require(['@/views/exam/exam/components/historyIndex'], resolve),
        name: '考试记录',
        meta: { title: '考试记录', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },

]

export default new Router({
  mode: 'history', // 去掉url中的#
  base: '/student/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
