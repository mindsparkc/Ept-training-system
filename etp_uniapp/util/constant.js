import config from '../common/config.js'
module.exports = {

	CLI_CURR: 'CLI_CURR', //点击课程
	CLI_COUR: 'CLI_COUR', //点击课件
	CLI_TASK: 'CLI_TASK', //点击任务
	CLI_EXAM: 'CLI_EXAM', //开始考试 后端调用


	CLI_EX_SUB: 'CLI_EX_SUB',
	CLI_EXAM_END: 'CLI_EXAM_END',
	CLI_TAG: 'CLI_TAG',

	ING_STUDY: 'ING_STUDY',

	//图片 https://10.153.3.22:4430/profile/app-res/wave.gif
	WAVE: config.baseUrl + '/profile/app-res/wave.gif',
	BLUE_CERT: config.baseUrl+ '/profile/app-res/bg_blue_cert.png',
	WHITE_CERT: config.baseUrl + '/profile/app-res/bg_white_cert.png',
	CERT: config.baseUrl + '/profile/app-res/cert.png',
	DEFAULT: config.baseUrl + '/profile/app-res/default.jpg',
	DOWNLOAD: config.baseUrl + '/profile/app-res/download.png',
	LIVE: config.baseUrl + '/profile/app-res/live.png',
	LOGIN: config.baseUrl + '/profile/app-res/login.png',


}
