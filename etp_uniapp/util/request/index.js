// 引入配置
// #ifndef H5
import config from '@/common/config'
// #endif

// 初始化请求配置
uni.$u.http.setConfig((defaultConfig) => {
	/* defaultConfig 为默认全局配置 */
	defaultConfig.baseURL = config.baseUrl /* 根域名 */ 
	// 请求头配置
	// #ifdef APP-PLUS
	defaultConfig.header=config.headerApp
	// #endif
	// #ifdef MP-WEIXIN
	defaultConfig.header=config.headerWx
	// #endif
	// #ifdef H5
	defaultConfig.header=config.headerH5
	// #endif
	
	
	return defaultConfig
})

module.exports = (vm) => {
	//请求拦截
	require('./requestInterceptors')(vm)
	//响应拦截
	require('./responseInterceptors')(vm)
}
