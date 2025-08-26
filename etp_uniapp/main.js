import Vue from 'vue'
//导入PC适配JS(H5)
// #ifdef H5
// import "./common/pc";
// #endif
// vuex
import App from './App'
import store from './store'

// 引入全局uView
import uView from '@/uni_modules/uview-ui'

import mixin from './common/mixin'

import FloatAsk from "@/components/float-ask/float-ask.vue"

Vue.prototype.$store = store


//课程封面等默认图片
Vue.prototype.teachCover= function(cover){
	if (cover!=null&&cover) {
		return cover;
	} else{
		return constant.DEFAULT
	}
}

Vue.config.productionTip = false
App.mpType = 'app'

Vue.use(uView)

Vue.component('FloatAsk', FloatAsk)

// #ifdef MP-WEIXIN
// 引入uView对小程序分享的mixin封装
const mpShare = require('@/uni_modules/uview-ui/libs/mixin/mpShare.js')
Vue.mixin(mpShare)
// #endif

Vue.mixin(mixin)

const app = new Vue({
	store,
	...App
})

// 引入请求封装
require('./util/request/index')(app)

//http接口api集中管理
import httpApi from '@/common/api.js'
Vue.use(httpApi, app)
//埋点统计
import point from '@/util/point.js'
Vue.prototype.$point = point;
//常量
import constant from '@/util/constant.js'
Vue.prototype.$constant = constant;
app.$mount()


//main.js
// Vue.prototype.$showimg = function (data) {
//   uni.navigateTo({
//     url: '/pages/showimg/showimg?imgs=' + JSON.stringify(data.imgs) + '&current=' + data.current,
//     animationType: "fade-in",
//   })
// }
