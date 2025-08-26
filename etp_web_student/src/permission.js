import router from './router'
import store from './store'
import {
  Message
} from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {
  getToken
} from '@/utils/auth'
import {
  isRelogin
} from '@/utils/request'

NProgress.configure({
  showSpinner: false
})

// 路由拦截 - 白名单，允许跳转 /index
// const whiteList = ['/login', '/auth-redirect', '/bind', '/register', '/index','/curriculumAll','/system/notice']
const whiteList = ['/login', '/auth-redirect']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({
        path: '/'
      })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0 ||
        store.getters.roles[0] === 'anonymity') { // 若角色为匿名用户角色，则重新获取菜单项
        isRelogin.show = true;
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false;
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({
              ...to,
              replace: true
            }) // hack方法 确保addRoutes已完成
            // 若是匿名用户，则移除浏览器端token
            if (store.getters.roles[0] === 'anonymity') {
              store.dispatch('LogOut').then(() => {
                Message.error(err)
                next({
                  path: '/'
                })
              })
            }
          })
        }).catch(err => {
          store.dispatch('LogOut').then(() => {
            Message.error(err)
            next({
              path: '/'
            })
          })
        })
      } else {
        next()
      }

    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // debugger
      // 在免登录白名单，直接进入
      store.dispatch('GetInfo').then(() => {
        store.dispatch('GenerateRoutes').then(accessRoutes => {
          // 根据roles权限生成可访问的路由表
          router.addRoutes(accessRoutes) // 动态添加可访问路由表
        })
      }).catch(err => {
        store.dispatch('LogOut').then(() => {
          Message.error(err)
          next({
            path: '/'
          })
        })
      })
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
