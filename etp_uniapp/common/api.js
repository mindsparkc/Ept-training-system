const http = uni.$u.http

const install = (Vue, vm) => {
	vm.$u.api = {
		//登录
		login: (params, config = {}) => http.post('/login', params, config),

		//修改密码
		changePassword: (params) => http.put('/system/user/profile/updatePwd' + params),

		//获取当前登录用户信息
		getUserInfo: () => http.get('/system/user/profile'),

		//获取验证码
		getCaptchImage: () => http.get('/captchaImage'),
		//注册用户
		register: (params, config = {}) => http.post('/register', params, config),
		
		//deepseek 
		streamChat: (data) => http.get('/system/deepseek/stream' ,{
			params: data
		}),
		//deepseek清除重置
		deleteRedisCache: () => http.post('/system/deepseek/reset'),

		//获取任务列表
		getTaskList: (data) => http.get('/etp/task/list', {
			params: data
		}),

		//获取任务标签
		getTaskTags: () => http.get('/etp/task/getTaskTags'),

		//获取任务内容
		getTaskInfo: (data) => http.get('/etp/task/' + data),

		//获取课程列表
		getLessonList: (data) => http.get('/etp/curriculum/list', {
			params: data
		}),
		//首页推荐课程 sftj 1推荐 0不推荐
		getRecommendList: (data) => http.get('/etp/curriculum/tjList', {
			params: data
		}),

		//领取课程
		receiveLesson: (data) => http.post('/etp/curriculum/receiveCurriculum', data),

		//获取首页推荐课程
		getHomeCurriculum: (data) => http.get('/etp/home/getHomeCurriculum', {
			params: data
		}),
		//获取知识库课程列表
		getKnowledgeList: (data) => http.get('/etp/curriculum/getKnowledgeInfo', {
			params: data
		}),

		//获取课程目录列表
		getLessonIndex: (data, config) => http.get('/etp/curriculum/selectTCurriculumByIds', {
			params: data
		}, config),

		//获取课程标签
		getLessonTags: () => http.get('/etp/curriculum/getCurriculumTags'),

		//查看课程试题信息
		getQuestionInfo: (data) => http.get('/etp/subject/' + data),

		//查看课件详情
		getCourswareInfo: (data, config) => http.get('/etp/courseware/' + data, config),


		//获取考试列表
		// getExamList: (data) => http.get('/etp/examination/list', {
		// 	params: data
		// }),
		getExamList: (data) => http.get('/user/v1/exam/list', {
			params: data
		}),


		//获取历史考试列表
		// getHistoryExamList: (data) => http.get('/etp/testpaperSubject/historyExam', {
		// 	params: data
		// }),
		getHistoryExamList: (data) => http.get('/user/v1/exam/history', {
			params: data
		}),

		//获取考试详情
		// getExamInfo: (data) => http.get('/etp/examination/' + data),
		getExamInfo: (data) => http.get('/user/v1/exam/' + data),

		//获取考试试卷关联题目  即开始考试
		// getExamQuestions: (data) => http.get('/etp/testpaperSubject/getSubjectByPaper', {
		// 	params: data
		// }),
		//获取考试试卷关联题目  即开始考试
		startExam: (data, config = {}) => http.post('/user/v1/exam/start', data, config),

		//提交试卷
		// handPaper: (data) => http.post('/etp/testpaperSubject/submitTheExam', data),
		handPaper: (data) => http.post('/user/v1/exam/submit', data),

		//提交单个题目的答案
		submitSingleQuestionAnswer: (recordId,data) => http.post(`/user/v1/exam/submit/answer/${recordId}`,data),

		//放弃本场考试
		giveupExam: (data) => http.post('/user/v1/exam/giveup/' + data),

		//获取考试成绩
		getExamGrade: (data) => http.get('/etp/testpaperSubject/resultInquiry/' + data),

		//获取用户答题信息
		getHistoryExam: (data) => http.get('/etp/testpaperSubject/selectAnswerInfo', {
			params: data
		}),
		//查看历史考试试卷&成绩
		getHistoryExamInfo: (data) => http.get('/user/v1/exam/record/' + data),


		getTaskAndExamNum: () => http.get('/etp/home/getTaskAndExamNum'),

		//获取轮播图
		getBanner: () => http.get('/etp/banner/list'),

		//获取首页问卷
		getHomeQuestionnaire: () => http.get('/etp/home/getHomeQuestionnaire'),
		//获取问卷列表
		getQuestionnaireList: (data) => http.get('/etp/questionnaire/list', {
			params: data
		}),

		//获取问卷详情
		getQuestionnaireInfo: (data) => http.get('/etp/questionnaire/' + data),

		//提交问卷
		submitQuestionnaire: (data) => http.put('/etp/questionnaire', data),


		//获取首页报名活动
		getHomeEnter: () => http.get('/etp/home/getHomeActivite'),

		//获取报名列表
		getEnterList: (data) => http.get('/etp/sign/list', {
			params: data
		}),

		//获取报名详情
		getEnterInfo: (data) => http.get('/etp/sign/' + data),

		//立即报名
		enter: (data) => http.put('/etp/sign', data),

		//获取通知列表
		getNoticeList: (data) => http.get('/system/notice/list', {
			params: data
		}),
		//获取通知详情 
		getNoticeInfo: (data) => http.get('/system/notice/' + data),


		//意见反馈
		feedback: (data) => http.post('/etp/opinion', data),

		//获取意见反馈
		getFeedbackList: (data) => http.get('/etp/opinion/list', {
			params: data
		}),

		getFeedback: (data) => http.get('/etp/opinion/' + data),


		//更新任务进度
		updateTaskStatus: (data) => http.post('/etp/task/updateTaskStatus', data),

		//更新课程进度
		updateStatus: (data) => http.post('/etp/curriculum/updateStatus', data),

		//点击事件埋点
		clickEvent: (data) => http.get('/etp/redisStream/sendRedis', {
			params: data
		}),
		//驻留事件埋点
		stayEvent: (data) => http.get('/etp/redisStream/sendRedisByIng', {
			params: data
		}),


		//错题列表
		getErrorBooks: (data) => http.get('/etp/errorbook/list', {
			params: data
		}),
		//删除错题
		deleteErrorBook: (data) => http.delete('/etp/errorbook/' + data),

		//课程保存习题
		saveSubject: (data) => http.post('/etp/curriculum/addSubject', data),

		//课程查询保存的用户习题答案
		getUserSubjectInfo: (lessonId, subjectId) => http.get('/etp/curriculum/getSubjectInfo/' + lessonId +
			"/" +
			subjectId),

		//版本更新
		updateVersion: (appCode, platform) => http.get('/app/appVersion/getNewApp/' + appCode + '&' + platform),

		//获取证书
		getCertList: () => http.get('/api/v1/record/certList'),


	};




}
export default {
	install
}
