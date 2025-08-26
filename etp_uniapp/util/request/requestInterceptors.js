/**
 * 请求拦截
 * @param {Object} http
 */
module.exports = (vm) => {
	uni.$u.http.interceptors.request.use((config) => { // 可使用async await 做异步操作
			// 初始化请求拦截器时，会执行此方法，此时data为undefined，赋予默认{}
			config.data = config.data || {}
			// 可以在此通过vm引用vuex中的变量，具体值在vm.$store.state中
			// console.log(vm.$store.state);

			if (config.url != '/login'&&config.url != '/captchaImage'&&config.url != '/register') {
				//若为登录和获取验证码则不添加session
				const token = uni.getStorageSync("token");
				if (token) {
					config.header.Authorization = token;
				} else { //token不存在  则取消本次请求 跳转到登录页面
					uni.$u.toast('请您登录');

					setTimeout(() => {
						uni.reLaunch({
							url: '/pages/login/login'
						});
					}, 1000)
					return Promise.reject(config)
				}
			}

			return config
		}, (config) => // 可使用async await 做异步操作
		Promise.reject(config))
}
