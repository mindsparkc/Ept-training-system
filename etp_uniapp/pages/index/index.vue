<!-- 首页 -->
<template>
	<view>

		<!-- <u-swiper :list="banner" previousMargin="30" nextMargin="30" :autoplay="true" radius="5" @click="clickBanner"
			bgColor="#ffffff" circular keyName="imageurl" ></u-swiper> -->
			
		<u-swiper :list="banner" previousMargin="30" nextMargin="30" :autoplay="true" radius="5"
			bgColor="#ffffff" circular keyName="imageurl" ></u-swiper>
		<view style="margin-top: 20px;">
			<u-grid :border="false" col="5" @click="toPath">
				<u-grid-item v-for="(item,index) in tabLists" :key="index">
					<view class="tab-bg">
						<image :src="item.icon" mode="widthFix" class="tab-image"> </image>
					</view>
					<text class="text-normal">{{item.name}}</text>
					<u-badge numberType="overflow" :type="item.type" max="99" :value="item.value" :showZero="false"
						:offset="[1,1]" :absolute="true"></u-badge>
				</u-grid-item>

			</u-grid>
		</view>


		<view class="notice-bg" hover-class="u-hover-class" @click="ToNoticeList">
			<text class="text-notice">通知
				<text class="text-notice-yellow">公告</text>
			</text>

			<u-notice-bar :text="noticeContent" direction="column" :icon="null" bgColor="transparent" color="#303133">
			</u-notice-bar>
		</view>


		<!-- 直播证书 -->
		<view class="flex-row">

			<image
				:src="livePng"
				@click="ToLive" class="img-live" mode="widthFix"></image>
			<image
				:src="certPng"
				@click="ToCert" class="img-cert" mode="widthFix"></image>
		</view>


		<!-- 报名问卷 已废弃-->
		<view class="flex-between" v-if="false">
			<view class="bg-yellow" hover-class="u-hover-class" @click="ToEnter()">
				<view class="flex-white-box">
					<text class="text-normal">{{enter.hdmc?enter.hdmc:"活动报名"}}</text>

					<text class="text-small-grey"
						v-if="enter.id">{{$u.timeFormat(formatTime(enter.kssj), 'mm/dd')}}至{{$u.timeFormat(formatTime(enter.jssj), 'mm/dd')}}</text>

					<text class="text-small-grey" v-else>暂无报名</text>
					<image src="../../static/index/icon-yellow-left.png" mode="widthFix" class="arrow-image"></image>

				</view>


			</view>
			<view class="bg-blue" hover-class="u-hover-class" @click="ToQuestionnare()">
				<view class="flex-white-box item">
					<view class="text-normal">{{questionnaire.wjmc?questionnaire.wjmc:"调查问卷"}}</view>
					<text class="text-small-grey"
						v-if="questionnaire.id">{{$u.timeFormat(formatTime(questionnaire.kssj), 'mm/dd')}}至{{$u.timeFormat(formatTime(questionnaire.jssj), 'mm/dd')}}</text>

					<text class="text-small-grey" v-else>暂无问卷</text>
					<image src="../../static/index/icon-blue-left.png" mode="widthFix" class="arrow-image"></image>
				</view>
			</view>

		</view>
		<!-- 当前任务 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class"
			@click="ToPath('/pages/taskList/taskList')" v-if="tasks&&tasks.length>0">
			<view class="div-relative">
				<text class="text-title">我的任务</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>

		</view>

		<view class="flex-between">
			<view v-for="(item,index) in tasks" :key="index" class="flex-col item" hover-class="u-hover-class"
				@click="ToTask(item)">
				<text class="label-rate">{{getTaskRate(item)}}</text>
				<u--image :src="teachCover(item.fmt)" :showLoading="true" mode="aspectFill" width="330rpx"
					height="180rpx" :fade="true" radius="5"></u--image>

				<view style="margin-top: 20rpx;">
					<u-text lines="1" :text="item.mc" :size="14"></u-text>
				</view>
				<!-- <text class="text-small-grey margin-top-small">已完成{{item.progress}}%</text> -->
			</view>
		</view>

		<!-- 学习进度 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class"
			@click="ToPath('/pages/lessonListIndex/lessonListIndex')" v-if="study&&study.length>0">
			<view class="div-relative">
				<text class="text-title">在线学习</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>

		</view>
		<view class="flex-between">
			<view v-for="(item,index) in study" :key="index" class="flex-col item" hover-class="u-hover-class"
				@click="ToLesson(item.id,item.mc)">
				<text class="label-rate">{{getLessonRate(item.jd)}}</text>
				<u--image :src="teachCover(item.fmt)" :showLoading="true" mode="aspectFill" width="330rpx"
					height="180rpx" :fade="true" radius="5"></u--image>
				<view style="margin-top: 20rpx;">
					<u-text lines="1" :text="item.mc" :size="14"></u-text>
				</view>
				<!-- <text class="text-small-grey margin-top-small">已完成{{item.progress}} 课时</text> -->
			</view>
		</view>

		<!-- 习题预热 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class"
			@click="ToPath('/pages/exercisesList/exercisesList')" v-if="exercises&&exercises.length>0">
			<view class="div-relative">
				<text class="text-title">习题预热</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>
		</view>
		<view v-for="(item,index) in exercises" :key="index" hover-class="u-hover-class" class="bg-exercises"
			@click="ToExam(item.examId)">
			<view class="silk-ribbon2">练习题</view>

			<view>
				<view class="text-ex-title">{{item.name}}
				</view>
				<view style="margin-top: 24rpx;"><text class="text-grey-content">{{item.name?item.name:""}}</text>

					<!-- <image style="width: 25rpx; height: 25rpx; vertical-align: middle;"
						src="../../static/exam/icon_exercises.png" mode="widthFix"></image>
					<text class="text-primary-content">共{{item.stgs?item.stgs:0}}题</text> -->
				</view>
			</view>

			<view class="btn-blue">去做题</view>


		</view>
		<!-- 考试进度 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class" style="margin-bottom: 40rpx;"
			@click="ToPath('/pages/exam/exam')" v-if="exams&&exams.length>0">
			<view class="div-relative">
				<text class="text-title">考试测验</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>

		</view>
		<view class="flex-between">
			<view v-for="(item,index) in exams" :key="index" class="flex-col item" hover-class="u-hover-class"
				@click="ToExam(item.examId)">
				<u--image :src="teachCover(item.cover)" :showLoading="true" mode="aspectFill" width="330rpx"
					height="180rpx" :fade="true" radius="5"></u--image>
				<view style="margin-top: 20rpx;margin-bottom: 20rpx;">
					<u-text lines="1" :text="item.name" :size="14"></u-text>
				</view>
				<!-- <text class="text-small-grey margin-top-small">已完成{{item.progress}}%</text> -->
			</view>
		</view>


		<!-- 推荐课程 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class" @click="ToTab('/pages/sort/sort')"
			v-if="lessons&&lessons.length>0">
			<view class="div-relative">
				<text class="text-title">推荐课程</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>
		</view>
		<view style="padding: 30rpx 30rpx 0rpx 30rpx;" v-if="lessons&&lessons.length>0">

			<scroll-view scroll-x style="white-space: nowrap;width: 100%;">
				<view style="display: flex;flex: row; ">
					<view v-for="(lessonItem,lessonIndex) in lessons" :key="lessonIndex" class="flex-col"
						hover-class="u-hover-class" @click="ToLesson(lessonItem.id,lessonItem.mc)"
						v-if="lessons&&lessons.length>0">
						<view style="margin-right: 20rpx;">
							<u--image :src="teachCover(lessonItem.fmt)" :showLoading="true" mode="aspectFill"
								width="280rpx" height="180rpx" :fade="true" radius="5"></u--image>

							<view style="margin-top: 20rpx;width: 280rpx;">
								<!-- <text class="k-text-ellipsis" style="width: 280rpx;">{{lessonItem.mc}}</text> -->
								<u-text lines="1" :text="lessonItem.mc" :size="14"></u-text>
							</view>
							<text v-if="false"
								class="text-small-grey margin-top-small">{{$u.timeFormat(formatTime(lessonItem.createTime), 'yyyy-mm-dd')}}</text>

						</view>
					</view>
				</view>
			</scroll-view>

		</view>

		<!-- 活动报名 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class" style="margin-bottom: 40rpx;"
			@click="ToPath('/pages/enterList/enterList')" v-if="enterList&&enterList.length>0">
			<view class="div-relative">
				<text class="text-title">活动报名</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>

		</view>
		<view class="flex-between">
			<view v-for="(item,index) in enterList" :key="index" class="flex-col" hover-class="u-hover-class"
				@click="ToEnterItem(item.id)" style="margin-right: 20rpx;">
				<u--image :src="teachCover(item.hdfm)" :showLoading="true" mode="aspectFill" width="330rpx"
					height="180rpx" :fade="true" radius="5"></u--image>
				<view style="margin-top: 20rpx;margin-bottom: 20rpx;">
					<u-text lines="1" :text="item.hdmc" :size="14"></u-text>
				</view>

			</view>
		</view>

		<!-- 问卷调查 -->
		<view class="flex-between flex-between__center" hover-class="u-hover-class" style="margin-bottom: 40rpx;"
			@click="ToPath('/pages/questionnaire/questionnaire')" v-if="questionnaires&&questionnaires.length>0">
			<view class="div-relative">
				<text class="text-title">问卷调查</text>
				<view class="div-absoult"></view>
			</view>
			<text class="text-small-grey">更多 </text>
			<u-icon name="arrow-right" color="#909399" size="14"></u-icon>

		</view>
		<view class="flex-between">
			<view v-for="(item,index) in questionnaires" :key="index" class="flex-col" hover-class="u-hover-class"
				@click="ToQuestionnare(item.id)" style="margin-right: 20rpx;">
				<u--image :src="teachCover(item.fm)" :showLoading="true" mode="aspectFill" width="330rpx"
					height="180rpx" :fade="true" radius="5"></u--image>
				<view style="margin-top: 20rpx;margin-bottom: 20rpx;">
					<u-text lines="1" :text="item.wjmc" :size="14"></u-text>
				</view>

			</view>
		</view>

		<u-loading-page loading-text="加载中..." loading-mode="spinner" :loading="!isLoaded"></u-loading-page>

		<float-ask></float-ask>
	</view>
</template>

<script>
	// import Fingerprint2 from 'fingerprintjs2'
	
	export default {
		data() {
			return {
				finger: '', //浏览器指纹
				isLoaded: false,
				banner: [

				],
				livePng:this.$constant.LIVE,
				certPng:this.$constant.CERT,
				//导航按钮
				tabLists: [{
						"icon": "../../static/index/icon-task.png",
						"link": "/pages/taskList/taskList",
						"name": "任务",
						"type": "error",
						"value": 0
					},
					{
						"icon": "../../static/index/icon-lesson.png",
						"link": "/pages/lessonListIndex/lessonListIndex",
						"name": "课程",
						"type": "error",
						"value": 0
					},
					{
						"icon": "../../static/index/icon-apply.png",
						"link": "/pages/exam/exam",
						"name": "考试",
						"type": "error",
						"value": 0
					},
					{
						"icon": "../../static/index/icon-exam.png ",
						"link": "/pages/exercisesList/exercisesList",
						"name": "习题",
						"type": "error",
						"value": 0
					},
					{
						"icon": "../../static/index/icon-question.png",
						"link": "/pages/questionnaire/questionnaire",
						"name": "问卷",
						"type": "error",
						"value": 0
					}
				],
				noticeContent: [], //通知公告内容
				noticeDate: "1642472936911", //通知公告发布时间

				lessons: [], //推荐课程
				tasks: [],
				study: [], //在线学习课程
				exams: [],
				enter: {},
				enterList: [],
				questionnaire: {},
				exercises: [], //习题
				questionnaires: [], //问卷

			}
		},
		onLoad() {
			this.getHomeData();
		},
		onPullDownRefresh() {
			this.getHomeData();
		},

		methods: {
			// getFinger() {
			// 	let that = this;
			// 	Fingerprint2.get(function(components) {
			// 		const values = components.map(function(component, index) {
			// 			if (index === 0) { //把微信浏览器里UA的wifi或4G等网络替换成空,不然切换网络会ID不一样
			// 				return component.value.replace(/\bNetType\/\w+\b/, '')
			// 			}
			// 			return component.value
			// 		})
			// 		// 生成最终id murmur   
			// 		const murmur = Fingerprint2.x64hash128(values.join(''), 31)
			// 		console.log(murmur);
			// 		that.finger = murmur;
			// 	});

			// },

			getHomeData() {
				this.getMyLesson();

			},
			//获取banner
			getBanner() {
				uni.$u.api.getBanner().then(res => {
					this.banner = res.data;
				}).catch(err => {
					console.log("getBanner", err.errMsg);

				})

				//banner展示推荐课程前三个，若没有推荐课程 使用banner  暂不用此逻辑
				// if (this.lessons && this.lessons.length > 0) {
				// 	let lessonBanners = this.lessons.slice(0, 3);
				// 	let banners = []
				// 	for (var i = 0; i < lessonBanners.length; i++) {
				// 		let banner = {};
				// 		banner.imageurl = lessonBanners[i].fmt ? lessonBanners[i].fmt :
				// 			'http://10.153.3.27:8880/profile/upload/2022/05/27/e3d0f778-4b50-4cd6-810d-8aac4ac1aaa3.jpg';//Fixme
				// 		banner.title = lessonBanners[i].mc ? lessonBanners[i].mc : '';
				// 		banner.id = lessonBanners[i].id
				// 		banners.push(banner);
				// 	}
				// 	this.banner = banners;
				// 	// console.log(JSON.stringify(this.banner));
				// } else {
				// 	uni.$u.api.getBanner().then(res => {
				// 		this.banner = res.data;
				// 		console.log(JSON.stringify(this.banner));
				// 	}).catch(err => {
				// 		console.log("getBanner", err.errMsg);

				// 	})
				// }


			},

			//获取推荐课程  调用知识库列表接口  更多跳转知识库
			getRecommendLesson() {
				let that = this;
				let params = {
					pageNum: 1,
					pageSize: 6,
					sftj: 1, //是否推荐 1推荐 0不推荐
				};
				uni.$u.api.getRecommendList(params).then(res => {
					this.lessons = res.rows || [];

				}).catch(err => {
					console.log(err.errMsg);
				})


			},
			//获取我的课程
			getMyLesson() {

				let params = {
					"pageNum": 1,
					"pageSize": 2,
				}
				uni.$u.api.getLessonList(params).then(res => {
					this.study = res.rows;
					//获取课程之后获取其他数据
					this.getData();
				}).catch(err => {
					uni.stopPullDownRefresh();

				});
			},
			//获取我的任务
			getMineTask() {
				let params = {
					"pageNum": 1,
					"pageSize": 2,
				}
				uni.$u.api.getTaskList(params).then(res => {
					this.tasks = res.rows;

				}).catch(err => {
					console.log("getMineTask>>>>" + err.errMsg);
				});

			},
			//获取活动报名
			getEnterList() {
				let params = {
					"pageNum": 1,
					"pageSize": 2,
				}
				uni.$u.api.getEnterList(params).then(res => {
					//截取前两个
					if (res.rows && res.rows.length > 2) {
						this.enterList = res.rows.slice(0, 2);
					} else {
						this.enterList = res.rows;
					}

				}).catch(err => {
					console.log("getEnterList>>>>" + err.errMsg);
				});

			},
			//获取问卷调查
			getQuestionnaireList() {
				let params = {
					"pageNum": 1,
					"pageSize": 2,
				}
				uni.$u.api.getQuestionnaireList(params).then(res => {
					//截取前两个
					if (res.rows && res.rows.length > 2) {
						this.questionnaires = res.rows.slice(0, 2);
					} else {
						this.questionnaires = res.rows;
					}

				}).catch(err => {
					console.log("getQuestionnaireList>>>>" + err.errMsg);
				});

			},
			//获取考试
			getExam() {
				let params = {
					"pageNum": 1,
					"pageSize": 2,
				}
				uni.$u.api.getExamList(params).then(res => {
					this.exams = res.rows;

				}).catch(err => {
					console.log("getExam>>>>" + err.errMsg);
				});

			},
			//获取习题
			getExercises() {
				let params = {
					"pageNum": 1,
					"pageSize": 2,
					"fb": -1
				}
				uni.$u.api.getExamList(params).then(res => {
					this.exercises = res.rows;

				}).catch(err => {
					console.log("getExam>>>>" + err.errMsg);
				});

			},
			//获取最新一条进行中并且没报名过的报名
			getNewEnter() {

				uni.$u.api.getHomeEnter().then(res => {
					//如果data为空则证明最近没有新的报名活动  显示暂无
					if (res.data) {
						this.enter = res.data;
					}


				}).catch(err => {
					uni.stopPullDownRefresh();

					console.log("getNewEnter", JSON.stringify(err));
				});

			},
			//获取最新一条进行中并且没填写过的问卷
			getNewQuestionnaire() {
				uni.$u.api.getHomeQuestionnaire().then(res => {
					if (res.data) {
						this.questionnaire = res.data
					}



				}).catch(err => {
					console.log("getNewQuestionnaire>>>>" + err.errMsg);
				});
			},
			//获取三条最新的通知公告
			getNoticeList() {

				let params = {
					"pageNum": 1,
					"pageSize": 3,
				}
				uni.$u.api.getNoticeList(params).then(res => {
					let notices = res.rows;
					let noticeTitles = [];
					this.noticeContent = [];
					for (let i = 0; i < notices.length; i++) {
						noticeTitles.push(notices[i].noticeTitle)
					}
					this.noticeContent = noticeTitles;

				}).catch(err => {
					uni.stopPullDownRefresh();
					console.log("getNoticeList>>>>" + err.errMsg);
				});
			},

			getData() {

				//获取轮播图
				this.getBanner();
				//获取我的任务
				this.getMineTask();
				//获取考试
				this.getExam();
				//获取习题
				this.getExercises();
				//获取最新一条问卷
				// this.getNewQuestionnaire();
				//获取三条最新的通知公告
				this.getNoticeList();
				//获取推荐课程
				this.getRecommendLesson();
				//获取活动报名
				this.getEnterList();
				//获取调查问卷
				this.getQuestionnaireList();
				this.isLoaded = true;
				uni.stopPullDownRefresh();
			},

			//点击banner
			clickBanner(index) {

				let item = this.banner[index]

				this.ToLesson(item.id, item.title)
			},
			//点击上方导航按钮
			toPath(index) {
				let link = this.tabLists[index].link

				uni.navigateTo({
					url: link,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},
			ToPath(path) {
				uni.navigateTo({
					url: path,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},

			ToTab(path) {
				uni.switchTab({
					url: path
				});
			},

			ToLesson(id, title) {

				// uni.navigateTo({
				// 	url: '/pages/lesson/lesson?id=' + id + '&title=' + title,
				// 	success: res => {},
				// 	fail: () => {},
				// 	complete: () => {}
				// });

				uni.navigateTo({
					url: '/pages/lessonIndexInfo/lessonIndexInfo?id=' + id + '&title=' + title,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

				this.$point.clickEvent(this.$constant.CLI_CURR, id);
			},
			ToTask(item) {
				uni.navigateTo({
					url: '/pages/taskIndex/taskIndex?taskId=' + item.id + '&cover=' + item.fmt + "&jd=" + item.jd,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
				this.$point.clickEvent(this.$constant.CLI_TASK, item.id);
			},
			ToExam(id) {
				uni.navigateTo({
					url: '/pages/exam/examIndex/examIndex?examId=' + id,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			ToEnterItem(enterId) {
				uni.navigateTo({
					url: '/pages/enter/enter?enterId=' + enterId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			ToEnter() {

				if (this.enter.id) {
					uni.navigateTo({
						url: '/pages/enter/enter?enterId=' + this.enter.id,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.showToast({
						title: '当前暂无进行中的报名',
						icon: 'none'
					});
				}
			},
			ToQuestionnare(questionId) {
				if (questionId) {
					uni.navigateTo({
						url: "/pages/questionnaire/questionBegin?id=" + questionId,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.showToast({
						title: '当前暂无问卷调查',
						icon: 'none'
					});
				}
			},
			ToNoticeList() {
				uni.navigateTo({
					url: '/pages/noticeList/noticeList',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			//返回时间戳
			formatTime(time) {
				return (new Date(time)).getTime();;
			},
			//获取课程进度
			getLessonRate(jd) {
				if (jd) {
					let jdObj = JSON.parse(jd);
					let rate = Math.round(jdObj.rate);
					if (rate) {
						if (rate == 100) {
							return "已学完";
						} else {
							return "已学" + Math.round(rate) + "%";
						}
					} else {
						return "未学习"
					}

				} else {
					return "未学习"
				}

			},

			//获取任务进度
			getTaskRate(item) {

				//判断任务状态
				let rwjd = "";
				//判断任务开始结束时间  显示未开始  已结束
				let beginTime = this.formatTime(item.taskBeginTime);
				let endTime = this.formatTime(item.taskEndTime);
				let now = new Date().getTime();
				if (now < beginTime) { //任务未开始
					rwjd = "未开始";
				} else if (now < endTime) { //任务进行中
					//判断任务的进度  展示已完成xx%  已完成
					let jd = item.jd;
					if (jd) {
						let rate = JSON.parse(jd).rate;
						if (rate) {
							rate = Math.round(rate);
							if (rate == 100) {
								rwjd = "已完成"
							} else {
								rwjd = "完成" + rate + "%";

							}
						} else {
							rwjd = "完成0%";
						}


					} else { //没有进度证明还没开始即已完成0%
						rwjd = "完成0%";

					}

				} else { //任务已结束
					rwjd = "已结束";

				}
				return rwjd;


			},
			ToLive() {

				uni.navigateTo({
					url: '/pages/empty/empty?title=直播预约',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			ToCert() {

				uni.navigateTo({
					url: '/pages/certList/certList',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}



		}
	}
</script>

<style lang="scss">
	.text-normal {
		color: $uni-text-color;
		font-size: $uni-font-size-base;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 2;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/
	}

	.text-small {
		color: $uni-text-color;
		font-size: $uni-font-size-sm;
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-base;
	}


	.text-lg {
		color: $uni-text-color;
		font-size: $uni-font-size-lg;
	}

	.text-title {
		color: $uni-text-color;
		font-size: $uni-font-size-lg;
		font-weight: bold;

	}

	.text-notice {
		color: $uni-color-primary;
		font-size: $uni-font-size-base;
		font-weight: bold;
		font-style: italic;

	}

	.text-notice-yellow {
		color: #FFD200;
		font-size: $uni-font-size-base;
		font-weight: bold;
		font-style: italic;
	}

	.notice-content {
		margin-left: 20rpx;
		margin-right: 20rpx;
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		flex: 1;

	}


	.tab-bg {
		box-shadow: 0 2px 4px 0 rgba(0, 125, 255, 0.2);
		background-color: rgba(0, 125, 255, 0.08);
		border-radius: 50%;
		height: 100rpx;
		width: 100rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.notice-bg {
		background-color: #F4f5f5;
		border-radius: 10rpx;
		padding: 20rpx 30rpx 20rpx 30rpx;
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: space-between;
		align-items: center;
		margin-top: 40rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;

	}


	.flex-between {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: space-between;
		margin-top: 40rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;


		&__center {
			align-items: center;
		}
	}

	/* #ifdef H5 */

	.flex-between {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: flex-start;
		margin-top: 40rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;


		&__center {
			align-items: center;
		}


	}

	.item:not(:first-child) {
		margin-left: 25px;
	}

	/* #endif */

	.flex-row {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
	}



	.flex-col {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		align-content: flex-start;
		width: 330rpx;
	}

	.flex-1 {
		flex: 1;
	}

	.flex-col-lesson-root {
		display: flex;
		flex-direction: column;
		margin-top: 24rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.flex-row-lesson {
		display: flex;
		flex-direction: row;
		// align-items: center;
		// justify-content: center;
	}

	.flex-col-lesson {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		align-content: space-between;
		margin-left: 20rpx;
	}

	.flex-white-box {

		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-around;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		padding: 16rpx 30rpx 16rpx 30rpx;
		min-height: 110rpx;
		overflow: hidden;
		// width: 0;
		flex: 1;
		min-width: 0;
	}

	.arrow {
		width: 22rpx;
		height: 16rpx;
	}

	.img-live {
		flex: 1 0;
		margin-top: 40rpx;
		margin-left: 30rpx;
		margin-right: 15rpx;


	}

	.img-live:active {
		opacity: 0.8;
	}

	.img-cert:active {
		opacity: 0.8;
	}

	.img-cert {
		flex: 1 0;
		margin-top: 40rpx;
		margin-left: 15rpx;
		margin-right: 30rpx;
	}

	.bg-yellow {
		background-image: url("https://vkceyugu.cdn.bspapp.com/VKCEYUGU-3e0b6c2c-e702-4719-9b5e-953fe029d4fe/527be0f0-9a1a-4d08-b72d-a7a75d70e2f6.png");
		background-repeat: no-repeat;
		padding: 28rpx;
		background-size: 100% 100%;
		border-radius: 10rpx;
		margin-right: 16rpx;
		flex: 1 0;
		min-width: 0;
		width: 0;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.bg-blue {
		background-image: url("https://vkceyugu.cdn.bspapp.com/VKCEYUGU-3e0b6c2c-e702-4719-9b5e-953fe029d4fe/a8f353c6-37d7-4d84-972f-4fc8c97c6adc.png");
		background-repeat: no-repeat;
		background-size: 100% 100%;
		margin-left: 16rpx;
		padding: 28rpx;
		border-radius: 10rpx;
		flex: 1 0;
		overflow: hidden;
		text-overflow: ellipsis;
		min-width: 0;
		width: 0;
	}

	.margin-top-normal {
		margin-top: 24rpx;
	}

	.margin-top-small {
		margin-top: 10rpx;
	}

	.line {
		margin-top: 24rpx;
		margin-bottom: 24rpx;
		background-color: #e4e7ed;
		height: 1rpx;

	}

	.tab-image {
		width: 25px;
		height: 25px;

	}

	.arrow-image {
		width: 22rpx;
		height: 16rpx;
	}

	.nav-image {
		width: 46rpx;
		height: 46rpx;
		padding: 10rpx;
	}

	.label-rate {
		border-top-right-radius: 10rpx;
		border-bottom-left-radius: 20rpx;
		background-color: RGBA(5, 196, 109, 0.8);
		padding: 4rpx 10rpx;
		color: #FFFFFF;
		font-size: 20rpx;
		position: absolute;
		text-align: center;
		right: 0;
		z-index: 99;
	}

	.bg-exercises {
		position: relative;
		background-color: white;
		border-radius: 10rpx;
		box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
		margin: 30rpx;
		padding: 24rpx 30rpx 24rpx 80rpx;
		display: flex;
		flex-direction: row;
		align-items: center;

	}

	.text-grey-content {
		max-width: 330rpx;
		color: #909199;
		font-size: 24rpx;
		margin-right: 20rpx;
	}

	.text-primary-content {
		color: #007DFF;
		font-size: 24rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
		width: 450rpx;
	}


	.text-ex-title {
		min-width: 430rpx;
		color: $uni-text-color;
		font-size: $uni-font-size-lg;

	}

	.silk-ribbon2 {
		display: inline-block;
		width: 43rpx;
		padding: 5rpx 0rpx;
		background: #FF9B19;
		top: -10rpx;
		left: 24rpx;
		position: absolute;
		text-align: center;
		border-top-left-radius: 5rpx;
		color: white;
		font-size: 24rpx;
		line-height: 27rpx;
	}

	.btn-blue {
		background: #E5F2FF;
		border-radius: 40rpx;
		color: #007DFF;
		font-size: 28rpx;
		height: 47rpx;
		flex-shrink: 0;
		padding: 10rpx 18rpx;
		text-align: center;
		margin-left: 30rpx;
	}

	.silk-ribbon2:before {
		height: 0;
		width: 0;
		border-bottom: 10rpx solid #8D5A20;
		border-right: 5px solid transparent;
		right: -5px;
		top: 0;
	}

	.silk-ribbon2:before,
	.silk-ribbon2:after {
		content: "";
		position: absolute;
	}

	.silk-ribbon2:after {
		height: 0;
		width: 0;
		border-left: 22rpx solid #FF9B19;
		border-right: 22rpx solid #FF9B19;
		border-bottom: 20rpx solid transparent;
		bottom: -20rpx;
		left: 0;
	}

	.div-absoult {
		background: linear-gradient(-90deg, #FFC566 0%, rgba(255, 175, 0, 0) 100%);
		border-radius: 8px;
		height: 12rpx;
		width: 80rpx;
		position: absolute;
		top: 38rpx;
		z-index: -1;
	}

	.div-relative {
		position: relative;
		flex: 1
	}
</style>
