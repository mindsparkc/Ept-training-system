module.exports = {
	//内网开发地址
	// baseUrl:'http://10.153.3.12:8880/prod-api',
	//内网开发地址 敏捷
	// baseUrl: 'http://10.153.3.27:8880/prod-api',

	//外网访问地址 小程序发布使用
	//baseUrl:'https://api.shundesoft.com:4430/prod-api',
	baseUrl:'https://study.kh3.cn:20011/prod-api',
	//客户地址
	// baseUrl: 'http://123.232.112.243:1003/prod-api/',

	//体验内网地址 内部演示使用 apk h5 映射外网地址为api.shundesoft.com
	// baseUrl:'https://10.153.3.22:4430/prod-api',


	headerWx: {
		'x-platform': 'WX'
	},
	headerApp: {
		'x-platform': 'MH'
	},
	headerH5: {
		'x-platform': 'H5'
	},

	timeout: 30000,

}
