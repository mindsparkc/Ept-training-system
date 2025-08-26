//此为H5配置文件  在manifest中配置h5 index.html模版路径为template.h5.html 并在其中加载该config.js文件
const config = {

	// baseUrl: 'http://10.153.3.27:8880/prod-api',
	// baseUrl: 'http://10.153.3.12:8880/prod-api',

	// baseUrl: 'http://123.232.112.243:1003/prod-api/',

	// baseUrl:'https://10.153.3.22:4430/prod-api',
	//外网访问地址 小程序发布使用
	//baseUrl: 'https://api.shundesoft.com:4430/prod-api',
	baseUrl:'https://study.kh3.cn:20011/prod-api',
	// baseUrl:'http://127.0.0.1:8080',
	//h5本地访问
	// baseUrl: '/prod-api',
	// baseUrl: '/',


	headerWx: {
		'x-platform': 'WX'
	},
	headerApp: {
		'x-platform': 'MH'
	},
	headerH5: {
		'x-platform': 'H5'
	}
}
