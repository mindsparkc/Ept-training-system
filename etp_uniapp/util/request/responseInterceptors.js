/**
 * 响应拦截
 * @param {Object} http 
 */
module.exports = (vm) => {
	uni.$u.http.interceptors.response.use((response) => {
		/* 对响应成功做点什么 可使用async await 做异步操作*/
		const data = response.data

		// 自定义参数
		const custom = response.config?.custom
		if (data.code !== 200) {
			if (data.code == 401) {
				uni.$u.toast('登录状态已失效');
				setTimeout(() => {
					// 此为uView的方法，详见路由相关文档
					// vm.$u.route('/pages/login/login')
					uni.reLaunch({
						url: '/pages/login/login'
					});
				}, 1500)
			} else {
				// 服务端返回的状态码不等于200，则reject()
				console.log(">>>>>>请求失败>>>>>>", data.msg);
				//对考试进行单独处理
				if (response.config.url.indexOf('/user/v1/exam') != -1&&data.code==500) {
					//若等于500 关闭当前页面跳转到错误页面 统一处理
					uni.redirectTo({
						url: '/pages/empty/error/error?errMsg=' + data.msg,
					});
				}

				// 如果没有显式定义custom的toast参数为false的话，默认对报错进行toast弹出提示
				if (custom.toast !== false) {
					uni.$u.toast(data.msg)
				}
				// 如果需要catch返回，则进行reject
				if (custom?.catch) {
					return Promise.reject(data)
				} else {
					// 否则返回一个pending中的promise
					return new Promise(() => {})
				}
			}

		}

		// console.log("响应体：", response);
		return data || {}
	}, (response) => {


		/*  对响应错误做点什么 （statusCode !== 200）*/
		//若为token失效则返回登录页面 
		if (response.statusCode == 401) {
			uni.$u.toast('登录状态已失效');
			setTimeout(() => {
				// 此为uView的方法，详见路由相关文档
				uni.reLaunch({
					url: '/pages/login/login'
				});
			}, 1500)
		} else {
			const token = uni.getStorageSync("token");
			let responseUrl = response.config?.url;
			let pointStayUrl = '/etp/redisStream/sendRedisByIng';
			let pointClickUrl = '/etp/redisStream/sendRedis';
			if (token && responseUrl != pointStayUrl && responseUrl != pointClickUrl) {
				uni.$u.toast("请求异常")
			}

		}

		return Promise.reject(response)
	})
}
