<!-- 考试主页面页面  -->
<template>
	<view class="page" @touchstart="touchStart()">
		<view class="header">
			<view class="cd-top">
				<view style="margin-right: 6rpx;">
					<u-icon name="clock" color="#0680FF" :bold="true"></u-icon>
				</view>

				<u-count-down ref='countdown' :time="examTime*60*1000" format="HH:mm:ss" @change="onChange"
					@finish="onFinish" v-if="examTime">

				</u-count-down>
				<text class="k-text-lg k-text-black k-text-bold" v-else>--:--</text>
			</view>

		</view>
		<view class="main">
			<kt-swiper-question ref="swiper" :list="list" :getPreviousHandler="gph" :getNextHandler="gnh"
				:dataIndex="currentIndex" @change="change" :isShowAnswer="false" @answer="answer">
			</kt-swiper-question>

		</view>

		<view class="footer">

			<view class="btn-pop" hover-class="u-hover-class" @click="judgeComplete()">交卷</view>
			<!-- 上一题 -->
			<view class="flex-row" hover-class="u-hover-class" @click="$u.throttle(onClickLast, 300,true)">

				<u-icon name="arrow-left" color="#303133" size="16"></u-icon>


				<text class="text-normal">上一题</text>
			</view>

			<!-- 下一题 -->
			<view class="flex-row" hover-class="u-hover-class" @click="$u.throttle(onClickNext, 300,true)">
				<text class="text-normal">下一题</text>
				<u-icon name="arrow-right" color="#303133" size="16"></u-icon>

			</view>
			<view class="row" @click="openAnswerCard" style="margin-right: 30rpx;">
				<u-icon name="grid" color="#919199" size="28"></u-icon>
				<text class="text-bold">{{currentIndex+1}}</text>
				<text class="text-grey">/{{list.length}}</text>

			</view>

		</view>

		<!-- 答题卡弹窗 -->
		<u-popup :show="showAnswerCard" @close="close" @open="open" mode="bottom">
			<view>
				<view class="top-pop">
					<view class="btn-pop" @click="judgeComplete()" hover-class="u-hover-class">交卷</view>
					<view class="row">
						<view class="done"></view>
						<text style="font-size: 20rpx;">已做</text>
						<view class="unDone"></view>
						<text style="font-size: 20rpx;">未做</text>
					</view>
					<view class="row" @click="close()" style="margin-right: 30rpx;">
						<u-icon name="grid" color="#919199" size="28"></u-icon>
						<text class="text-bold">{{currentIndex+1}}</text>
						<text class="text-grey">/{{list.length}}</text>

					</view>

				</view>
				<!-- 三种状态  未做  做了 选中currentIndex==index -->
				<view class="answer-grid">
					<view v-for="(cardItem,cardIndex) in list" :key="cardIndex"
						@click="onClickAnswerCardItem(cardIndex)">
						<view :class="answerCardItemState[answerCardState(cardIndex)]" class="answer-card">
							{{cardIndex+1}}
						</view>

					</view>
				</view>
			</view>
		</u-popup>

		<u-popup :show="showCountdownPopup" mode="center" round="20" :closeOnClickOverlay="false">
			<view class="flex-row-center">
				<text class="text-popup-title-red">考试时间到</text>
				<text class="text-popup-subtitle"> 请停止一切操作即将为您交卷...</text>
				<view style="margin-top: 20rpx;">
					<u-loading-icon color="#E91A1B"></u-loading-icon>
				</view>
				<image src="../../../static/exam/icon-count-down.png" class="img-count-done"></image>
				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

			</view>
		</u-popup>
		<u-popup :show="showDonePopup" mode="center" round="20">
			<view class="flex-row-center">
				<text class="text-popup-title">试题已做完</text>
				<text class="text-popup-subtitle"> 是否确认交卷</text>
				<image src="../../../static/exam/icon-exam-done.png" class="img-done"></image>

				<text class="text-popup-tips" v-if="examTime"> 考试剩余时间 {{timeData.days!=0?timeData.days+'天':''}}
					{{timeData.hours}}:{{timeData.minutes}}:{{timeData.seconds}}</text>

				<text class="text-popup-tips"> 文明考风 诚信考试 </text>
				<view class="flex-row-between">
					<view class="btn-normal" hover-class="u-hover-class" @click="showDonePopup=false">暂不交卷</view>
					<view class="btn-primary" hover-class="u-hover-class" @click="handPaper()">现在交卷</view>
				</view>

			</view>
		</u-popup>

		<u-popup :show="showUnDonePopup" mode="center" round="20">
			<view class="flex-row-center">
				<text class="text-popup-title-red">试题未做完</text>
				<text class="text-popup-subtitle"> 是否确认交卷</text>
				<image src="../../../static/exam/icon-exam-undone.png" class="img-done"></image>

				<text class="text-popup-tips" v-if="examTime"> 考试剩余时间 {{timeData.days!=0?timeData.days+'天':''}}
					{{timeData.hours}}:{{timeData.minutes}}:{{timeData.seconds}}</text>

				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

				<view class="flex-row-between">
					<view class="btn-normal" hover-class="u-hover-class" @click="showUnDonePopup=false">继续答题</view>
					<view class="btn-primary" hover-class="u-hover-class" @click="handPaper()">现在交卷</view>
				</view>

			</view>
		</u-popup>


		<u-popup :show="showExitPupup" mode="center" round="20">
			<view class="flex-row-center">
				<text class="text-popup-title-red">是否确认退出</text>
				<text class="text-popup-subtitle">在考试期间您可以重新进入本次考试\n到达考试时间后系统会默认为您交卷</text>
				<image src="../../../static/exam/icon-exam-undone.png" class="img-done"></image>

				<text class="text-popup-tips" v-if="examTime"> 考试剩余时间 {{timeData.days!=0?timeData.days+'天':''}}
					{{timeData.hours}}:{{timeData.minutes}}:{{timeData.seconds}}</text>

				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

				<view class="flex-row-between">
					<view class="btn-normal" hover-class="u-hover-class" @click="exitExam()">确认退出</view>
					<view class="btn-primary" hover-class="u-hover-class" @click="closeAllPopup()">继续考试</view>
				</view>

			</view>
		</u-popup>

		<u-loading-page loading-text="考试载入中" loading-mode="spinner" :loading="!isLoaded"></u-loading-page>
		<u-notify ref="handMinTime"></u-notify>
		<!-- 考试作弊提示 -->
		<!-- 切屏作弊提示 -->
		<u-notify ref="cheat"></u-notify>
		<!-- 长时间不操作提示 -->
		<u-notify ref="outTime"></u-notify>
		<!-- 切屏次数将要达到上限弹窗提示 -->
		<u-popup :show="showCheatTipsPopup" mode="center" round="20">
			<view class="flex-row-center">
				<text class="text-popup-title-red">郑重警告</text>
				<text class="k-text-md k-text-black k-margin-top-m">{{sysTips}}</text>
				<uni-icons type="clear" size="150" color="#E91A1B"></uni-icons>

				<text class="text-popup-tips" v-if="examTime"> 考试剩余时间{{timeData.days!=0?timeData.days+'天':''}}
					{{timeData.hours}}:{{timeData.minutes}}:{{timeData.seconds}}</text>
				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

				<view class="flex-row-between">

					<view class="btn-primary" style="background:#E91A1B" hover-class="u-hover-class"
						@click="showCheatTipsPopup=false">本人已知晓</view>
				</view>

			</view>
		</u-popup>
		<!-- 切屏次数将已达到上限弹窗提示  不允许点击关闭-->
		<u-popup :show="showCheatHeadPaperTipsPopup" mode="center" round="20" :closeOnClickOverlay="false">
			<view class="flex-row-center">
				<text class="text-popup-title-red">切屏次数已达上限</text>
				<text class="text-popup-subtitle"> 请停止操作即将为您交卷...</text>
				<view style="margin-top: 20rpx;">
					<u-loading-icon color="#E91A1B"></u-loading-icon>
				</view>
				<image src="../../../static/exam/icon-count-down.png" class="img-count-done"></image>
				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

			</view>
		</u-popup>
		<u-popup :show="showTimeoutHeadPaperTipsPopup" mode="center" round="20" :closeOnClickOverlay="false">
			<view class="flex-row-center">
				<text class="text-popup-title-red" style="width: 80%;">长时间未操作提醒</text>
				<text class="text-popup-subtitle">系统检测到您长时间未进行操作\n若{{leaveTimeChange}}后仍未操作将为您交卷处理</text>
				<view style="margin-top: 20rpx;">
					<u-loading-icon color="#E91A1B"></u-loading-icon>
				</view>
				<image src="../../../static/exam/icon-count-down.png" class="img-count-done"></image>
				<!-- 设置继续考试  交卷倒计时按钮 	 -->
				<button @click="goonExam()" class="k-btn">继续考试</button>
				<text class="text-popup-tips"> 文明考风 诚信考试 </text>

			</view>
		</u-popup>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				isLoaded: false, //是否获取
				loadingText: "试卷加载中...", //考试加载中  加载失败 出现错误

				canExit: false, //是否可以退出考试
				showScore: "", //成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
				showScoreTime: "", //如果时定时发布成绩，设置发布成绩的时间

				examInfo: {},
				examId: 0,
				recordId: 0,
				password: '', //考试口令密码
				paperId: 0,
				examTime: 0, //考试倒计时

				timeData: {},
				list: [],
				current: 0, //swiper组件的current值，表示当前那个swiper-item是活动的
				currentIndex: 0,
				swiperDuration: "250",
				showAnswerCard: false, //是否显示答题卡
				showDonePopup: false, //完成提示弹窗
				showUnDonePopup: false, //未完成提示弹窗

				showCountdownPopup: false, //考试时间到
				showExitPupup: false, //是否退出考试弹窗

				swiperHeight: 0,
				answerCardItemState: [
					'answer-undo', //未做
					'answer-done', //已做
					'answer-undo-select', //未做被选中
					'answer-done-select', //已做被选中
				], //答题卡状态class

				//考试防作弊 
				hideShowCount: 0, //页面隐藏显示次数  当次数大于等于系统配置次数时提示 请勿多次切屏 次数超过xx次将立即为您交卷
				hideShowCountRole: 0,
				showTimeoutHeadPaperTipsPopup: false, //长时间未操作交卷
				showCheatHeadPaperTipsPopup: false, //作弊交卷
				showCheatTipsPopup: false, //作弊弹窗
				sysTips: "系统检测到您考试期间切屏次数过多\n若再次切屏本次考试将按照作弊处理", //弹窗中的系统提示  用于切屏次数多  长时间无操作规则   系统检测到您考试期间长时间未操作已为您交卷处理
				timeoutTimer: null, //用户无操作计时器
				timeoutTime: 0, //用户无操作允许时长
				noActiveTime: 0, //用户无操作时长

				paperUnitOriginList: [], //初始题目列表

				submitQuestions: [], //待上传用户题目答案列表
				submitTimer: null, //定时上传用户答案定时器

				logicEndTime: null, //是否是从考试记录中进入 若为考试记录中进入  考试倒计时时间需要修改
				leaveTime: 30, //30s倒计时
				leaveTimeChange: "",
				leaveCountDownTimer: null,

			}
		},
		onLoad(option) {
			this.examId = option.examId;
			this.password = option.password;
			this.recordId = option.recordId;
			this.logicEndTime = option.logicEndTime;

			this.swiperHeight = uni.getSystemInfoSync().windowHeight;
			this.requestQuestionInfo();
			//启动定时上传用户答案定时器
			this.submitTimerTask();

			// #ifdef MP  
			//微信小程序无法响应onBackPress时间  只能通过此api 监听返回键的返回
			wx.enableAlertBeforeUnload({
				message: "退出考试您现有的答题结果可能会丢失\n是否确认退出",
				complete: (e) => {}
			})
			// #endif
		},
		onReady() {

		},
		onHide() {
			this.hideShowCount++
		},
		onUnload() {
			clearInterval(this.timeoutTimer)
			clearInterval(this.submitTimer)
			clearInterval(this.leaveCountDownTimer)

			this.timeoutTimer = null;
			this.submitTimer = null;
			this.leaveCountDownTimer = null;
		},

		onShow() {

			//首次切屏时使用顶部弹窗提示不允许切屏  当多次切屏时 使用popup提示
			//判断切屏次数  小于hideShowCountRole时弹窗警告一次 大于等于hideShowCountRole直接提示交卷
			if (this.hideShowCountRole) {

				let currentCount = this.hideShowCount;
				let totalCount = this.hideShowCountRole;
				if (currentCount == (totalCount - 1)) { //弹窗提示警告

					// 考试过程中请保持App前台运行,系统检测您切屏次数过多,n次切屏后系统将为您强制交卷
					this.showCheatTipsPopup = true;
				} else if (currentCount >= totalCount) { //弹窗提示并交卷
					this.showCheatHeadPaperTipsPopup = true;

					setTimeout(() => {
						this.showCheatHeadPaperTipsPopup = false;
						//交卷 
						this.handPaper()

					}, 3000)
					//TODO  作弊处理
				} else if (currentCount >= 1) {
					//每一次切屏顶部弹窗提示
					this.$refs.cheat.show({
						top: 10,
						type: 'warning',
						message: '警告：考试期间不允许切屏，切屏次数大于' + totalCount + '次按作弊处理',
						duration: 1000 * 3,

					})
				}
			}

		},
		onBackPress(e) {
			if (!this.canExit) { //若不能退出考试则拦截
				//若考试时间到的弹窗正在显示 则不显示退出弹窗
				if (!this.showCountdownPopup) {
					this.showExitPupup = true;
				}


				return true;
			}



		},
		methods: {
			gph: () => {
				//当前为第一个滑动触发事件
				uni.showToast({
					title: '当前为第一题',
					icon: 'none'
				});

			},
			gnh: () => {
				uni.showToast({
					title: '当前为最后一题',
					icon: 'none'
				});

			},
			change(currentIndex) {
				this.currentIndex = currentIndex


			},

			//倒计时 time  剩余时间
			onChange(e) {
				this.timeData = e
			},
			//退出考试
			exitExam() {
				this.canExit = true;
				uni.navigateBack({
					delta: 2
				});
			},

			closeAllPopup() {
				this.showAnswerCard = false;
				this.showDonePopup = false;
				this.showUnDonePopup = false;
				this.showExitPupup = false;
			},

			//倒计时结束
			onFinish() {
				//时间到 关闭弹窗
				this.closeAllPopup();
				//打开交卷弹窗  并在3秒后交卷
				this.showCountdownPopup = true;
				setTimeout(() => {
					//考试时间到直接交卷并退出考试
					this.handPaper();
				}, 2000)


			},
			//格式化题目列表 从按照各个单元排列 修改为打散排列
			formatQuestionList(paperUnit) {

				let allQuestion = []

				for (let i = 0; i < paperUnit.length; i++) {

					let paperUnitName = paperUnit[i].display;
					if (paperUnit[i].questions && paperUnit[i].questions.length > 0) {
						let paperUnitTotal = paperUnit[i].questions.length;
						let questions = paperUnit[i].questions;

						for (let j = 0; j < questions.length; j++) {
							questions[j].paperUnitName = paperUnitName;
							questions[j].paperUnitTotal = paperUnitTotal;
							questions[j].paperUnitSort = j + 1;
						}

						allQuestion = allQuestion.concat(questions)
					}

				}

				// console.log(JSON.stringify(allQuestion));

				for (let k = 0; k < allQuestion.length; k++) {
					let item = allQuestion[k];
					item.index = k;
					item.total = allQuestion.length;
				}

				//初始化数据
				this.list = allQuestion;
				this.swiperDuration = '250'

			},


			requestQuestionInfo() {
				//接口请求数据 
				let params = {
					examId: this.examId,
					recordId: this.recordId,
					password: this.password

				}
				uni.$u.api.startExam(params).then(res => {
					if (res.data) {
						this.isLoaded = true;
					}
					this.examInfo = res.data;
					if (this.logicEndTime) { //由考试记录进入的考试，时间要用逻辑结束时间减去本地时间

						let logic = Date.parse(this.logicEndTime)
						let date = new Date().getTime();

						let sy = Math.round((logic - date) / 60000);

						this.examTime = sy;
					} else {
						this.examTime = this.examInfo.totalTime; //考试限时  单位分钟	
					}


					this.showScore = this.examInfo.showScore;
					this.showScoreTime = this.examInfo.showScoreTime;
					this.hideShowCountRole = this.examInfo.leaveLimit; //允许离开考试次数
					this.timeoutTime = this.examInfo.noActLimit; //允许无操作的时间

					if (this.timeoutTime) {
						//启动无操作定时器
						this.onTouchTimeoutLinstener();
					}


					uni.setNavigationBarTitle({
						title: this.examInfo.name
					});


					this.paperUnitOriginList = this.examInfo.paperUnit;
					this.formatQuestionList(this.paperUnitOriginList)

				}).catch(err => {
					console.log(JSON.stringify(err));
				})

			},

			//下一题
			onClickNext() {

				this.$refs.swiper.goNext()
			},
			onClickLast() {
				this.$refs.swiper.goPrevious()
			},


			onClickAnswerCardItem(index) {
				this.swiperDuration = '0'; //关闭动画
				this.currentIndex = index;
				this.close()
			},

			//打开答题卡
			openAnswerCard() {
				this.showAnswerCard = true;
			},
			//答题卡打开时的回调
			open() {},
			//答题卡关闭回调
			close() {
				this.swiperDuration = '250'; //开启动画
				this.showAnswerCard = false;
			},
			//点击左下角交卷按钮  判断是否答题完毕可以交卷
			judgeComplete() {
				//首先判断答题时长  若答题时长不满足要求 则不可交卷

				let remainingTime = this.timeData.days * 60 * 24 + this.timeData.hours * 60 + this.timeData.minutes;
				if ((this.examInfo.totalTime - remainingTime) < this.examInfo.handMinTime) {

					this.$refs.handMinTime.show({
						top: 10,
						type: 'warning',
						message: '考试开始' + this.examInfo.handMinTime + "分钟后方可交卷",
						duration: 1000 * 3,

					})
					return;
				}



				let isComplete = true;
				for (let i = 0; i < this.list.length; i++) {
					if (!this.list[i].answeredFlag) {
						isComplete = false;
					}

				}
				if (isComplete) {
					this.openDonePopup()
				} else {
					this.openUnDonePopup()
				}

			},


			//弹窗显示询问是否交卷
			openDonePopup() {
				this.showDonePopup = true;
			},
			openUnDonePopup() {
				this.showUnDonePopup = true;
			},
			//交卷
			handPaper() {
				this.showDonePopup = false;
				this.showUnDonePopup = false;
				this.submitPaper()

			},
			//交卷
			submitPaper() {
				//停止倒计时
				if (this.$refs.countdown) {
					this.$refs.countdown.pause();
				}

				uni.showLoading({
					title: '正在交卷...',
					mask: true
				});
				this.examInfo.paperUnit = this.paperUnitOriginList; //赋值
				// console.log("需要提交的数据", JSON.stringify(this.examInfo));
				let params = this.examInfo;
				uni.$u.api.handPaper(params).then(res => {
					if (res.code == 200) {
						uni.hideLoading()
						this.closeAllPopup()

						//成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
						let showScore = res.data.showScore;
						switch (showScore) {
							case "0": //不显示成绩 
								this.goToSubmitSuccess();
								break;
							case "1": //跳转到查看成绩的页面 
								uni.redirectTo({
									url: '/pages/exam/examGrade/examGrade?examId=' + this
										.examId + "&recordId=" + this.examInfo.recordId,
									success: res => {},
									fail: () => {},
									complete: () => {}
								});
								break;
							case "2": //批改后显示
								this.goToSubmitSuccess();
								break;
							case "3": //定时发布
								this.goToSubmitSuccess();
								break;
							case "4": //人工发布
								this.goToSubmitSuccess();
								break;
							default:
								break;
						}

					} else {
						//交卷失败 
						uni.hideLoading()


						//定时重新交卷
						setTimeout(() => {
							this.submitPaper();
						}, 1000);

					}


				}).catch(err => {
					uni.hideLoading()
					console.log(err);
				})
			},
			//跳转到交卷成功页面
			goToSubmitSuccess() {
				uni.redirectTo({
					url: "/pages/empty/examSubmitSucess/examSubmitSucess?showScore=" + this.showScore +
						"&showScoreTime=" + this.showScoreTime + "&recordId=" + this.examInfo.recordId

				})
			},

			//判断是否做题
			answerCardState(index) {
				//答题卡状态 0 未做 1已做 2未做被选中 3 已做被选中 
				if (this.currentIndex == index) { //被选中
					if (this.list[index].answeredFlag) { //已做被选中
						return 3
					} else { //未做被选中
						return 2
					}
				} else {
					if (this.list[index].answeredFlag) {
						return 1
					} else {
						return 0
					}
				}
			},
			//返回时间戳
			formatTime(time) {
				return (new Date(time)).getTime();;
			},
			//用户触摸事件
			touchStart() {
				this.noActiveTime = 0;
				
			},
			//用户长时间无操作
			onTouchTimeoutLinstener() {

				this.$refs.outTime.show({
					top: 10,
					type: 'warning',
					message: '长时无操作定时器已开启，若' + this.examInfo.noActLimit + "分钟未操作系统将直接为您交卷",
					duration: 1000 * 3,

				})
				this.startTimeoutTimer();

			},
			//开启长时间无操作定时器
			startTimeoutTimer() {
				//初始化
				if (this.timeoutTimer) {
					clearInterval(this.timeoutTimer)
					this.timeoutTimer = null;
					this.noActiveTime = 0;
				}
				this.timeoutTimer = setInterval(() => {
					this.noActiveTime++;
					if (this.noActiveTime/60 >= (this.timeoutTime)) {

						//交卷弹窗 
						this.showTimeoutHeadPaperTipsPopup = true;

						this.leaveCountDown(); //开启交卷倒计时

					}

				},1000); //定时1s一次
			},


			//子组件回传
			answer(value) {

				//answeredFlag学员是否答题  answered学员问答题答案 
				this.list[this.currentIndex] = value; //更新question list
				//更新paperUnit  
				//首先根据paperUnitId找到paperUnit
				let paperUnitIndex = (this.paperUnitOriginList || []).findIndex((item) => item.paperUnitId === value
					.paperUnitId);
				let paperUnitQuestions = this.paperUnitOriginList[paperUnitIndex].questions;

				let questionIndex = (paperUnitQuestions || []).findIndex((item) => item.quId === value.quId);
				// 删除value中增加的属性防止接口出错
				let originValue = uni.$u.deepClone(value); //深拷贝
				this.$delete(originValue, "paperUnitName")
				this.$delete(originValue, "paperUnitTotal")
				this.$delete(originValue, "paperUnitSort")
				this.$delete(originValue, "index")
				this.$delete(originValue, "total")
				this.paperUnitOriginList[paperUnitIndex].questions[questionIndex] = originValue;

				//将待上传的答案加入到答案数组中
				//组合题单独处理  组合题需要将里面的题目单个上传
				let type = originValue.type;
				if (type == 6) { //组合题
					let children = originValue.children;
					for (let c = 0; c < children.length; c++) {
						let submitChildIndex = (this.submitQuestions || []).findIndex((item) => item.quId === children[c]
							.quId); //判断答案数组中有无该答案
						if (submitChildIndex != -1) { //存在则替换
							this.submitQuestions.splice(submitChildIndex, 1, children[c])
						} else { //不存在则添加
							this.submitQuestions.push(children[c])
						}

					}
				} else {
					let submitIndex = (this.submitQuestions || []).findIndex((item) => item.quId === originValue
						.quId); //判断答案数组中有无该答案
					if (submitIndex != -1) { //存在则替换
						this.submitQuestions.splice(submitIndex, 1, originValue)
					} else { //不存在则添加
						this.submitQuestions.push(originValue)
					}
				}






			},


			//定时器定时3s上传一次，答题数组，3s上传一次，上传成功的从数组中删除，每次答题结果加入数组时先判断数组中有没有，有则替代没有则增加，fixme
			submitTimerTask() {

				this.submitTimer = setInterval(() => {
					//提交单个题目的答案			
					let recordId = this.examInfo.recordId;
					if (this.submitQuestions && this.submitQuestions.length > 0) {
						let submitQuestion = this.submitQuestions[0];

						uni.$u.api.submitSingleQuestionAnswer(recordId, submitQuestion).then(res => {
							//移除并返回列表第一项
							this.submitQuestions.shift()
						}).catch(err => {
							console.log(err);


						})
					}



				}, 5 * 1000);

			},



			submitSingleQuestionAnswer(question) {
				let recordId = this.examInfo.recordId;
				uni.$u.api.submitSingleQuestionAnswer(recordId, question).then(res => {}).catch(err => {
					console.log(err);
				})
			},
			leaveCountDown() {
				if (this.leaveTime != 0) {
					if (this.leaveCountDownTimer) {
						clearInterval(this.leaveCountDownTimer);
						this.leaveCountDownTimer = null;
					}
					if (this.timeoutTimer) {
						clearInterval(this.timeoutTimer)
						this.timeoutTimer = null;
						this.noActiveTime = 0;
					}
					let limitTime = 30;
					this.leaveCountDownTimer = setInterval(() => {
						limitTime--;

						this.leaveTimeChange = limitTime + 's';
						if (limitTime == 0) {
							this.showTimeoutHeadPaperTipsPopup = false; //关闭弹窗
							//交卷
							this.handPaper()
						}
					}, 1000);

				}
			},
			//超时继续考试
			goonExam() {
				//关闭弹窗
				this.showTimeoutHeadPaperTipsPopup = false;
				//初始化时间不活动时间为0s
				this.noActiveTime = 0;
				//暂停倒计时交卷定时器
				if (this.leaveCountDownTimer) {
					clearInterval(this.leaveCountDownTimer);
					this.leaveCountDownTimer = null;
				}
				//重新开启长时间无操作计时器
				this.startTimeoutTimer();

			}

		}
	}
</script>

<style lang="scss">
	.page {
		background-color: #f8f8f8;
	}

	.header {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		z-index: 99;
		background-color: #FFFFFF;

	}

	.main {}

	.footer {
		background-color: #FFFFFF;
		width: 100%;
		position: absolute;
		bottom: 0;
		z-index: 99;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		padding-top: 8px;
		padding-bottom: 18px;
		box-shadow: 0 -1rpx 6rpx rgba(0, 0, 0, 0.1);
	}

	.cd-top {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		margin-top: 16rpx;
		margin-bottom: 16rpx;
	}


	.line {
		width: 100%;
		height: 1rpx;
		background-color: #E5E5E5;
	}

	.row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.text-bold {
		font-size: $uni-font-size-lg;
		font-weight: bold;
		color: $uni-text-color;
		margin-left: 6rpx;
	}

	.text-grey {
		font-size: $uni-font-size-lg;
		color: $uni-text-color-grey;
	}

	.text-normal {
		font-size: $uni-font-size-lg;
		color: #303133;
		font-weight: bold;
	}

	.top-pop {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding-top: 16rpx;
	}



	.done {
		width: 30rpx;
		height: 14rpx;
		background-color: #037EFF;
		border-radius: 4rpx;
		margin-right: 3rpx;
	}

	.unDone {
		width: 30rpx;
		height: 14rpx;
		margin-right: 3rpx;
		margin-left: 30rpx;
		border-radius: 4rpx;
		background-color: #DBF2FD;

	}

	.btn-pop {
		background-color: #007DFF;
		margin-left: 30rpx;
		border-radius: 50rpx;
		color: #FFFFFF;
		padding: 10rpx 20rpx;
		font-size: $uni-font-size-base;
	}

	.answer-grid {
		display: flex;
		flex-direction: row;
		align-items: center;
		flex-wrap: wrap;
		justify-content: flex-start;
		margin: 15rpx;
	}

	.answer-card {
		border-radius: 50%;
		width: 80rpx;
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 28rpx;
		margin: 16rpx;
	}

	//答题卡状态  已做  未做  已做被选中  未做被选中DBF2FD #0058b0
	.answer-done {
		border: solid 2rpx #037EFF;
		background-color: #037EFF;
		color: #FFFFFF;
	}

	.answer-done-select {
		border: solid 2rpx #0000ff;
		background-color: #037EFF;
		color: #FFFFFF;
	}

	.answer-undo {
		border: solid 2rpx #DBF2FD;
		background-color: #DBF2FD;
		color: #037EFF;
	}

	.answer-undo-select {
		border: solid 2rpx #037EFF;
		background-color: #DBF2FD;
		color: #037EFF;
	}




	.text-popup-title {
		font-size: 40rpx;
		font-weight: bold;
		color: #037EFF;
	}

	.text-popup-title-red {
		font-size: 40rpx;
		font-weight: bold;
		color: #E91A1B;
		text-align: center;
	}

	.text-popup-subtitle {
		font-size: 28rpx;
		font-weight: 400;
		margin-top: 20rpx;
		color: #303133;
	}

	.text-popup-tips {
		font-size: 24rpx;
		font-weight: 400;
		color: #919199;
	}

	.text-popup-tips-small {
		font-size: 20rpx;
	}

	.btn-primary {
		border-radius: 40rpx;
		background-color: #037EFF;
		color: white;
		flex: 1;
		width: 180rpx;
		margin-top: 50rpx;
		margin-left: 20rpx;
		margin-right: 30rpx;
		padding: 20rpx;
		font-size: 28rpx;
		text-align: center;
	}

	.btn-normal {
		flex: 1;
		border-radius: 40rpx;
		background-color: rgba(0, 125, 255, 0.1);
		padding: 20rpx;
		color: #037EFF;
		margin-top: 50rpx;
		width: 180rpx;
		margin-right: 20rpx;
		margin-left: 30rpx;
		font-size: 28rpx;
		text-align: center;

	}

	.flex-row-between {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		margin-top: 30rpx;
	}

	.flex-row-center {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.img-done {
		width: 300rpx;
		height: 200rpx;
		margin-top: 24rpx;
		margin-bottom: 24rpx;
	}

	.img-count-done {
		width: 400rpx;
		height: 400rpx;

	}

	.small-image {
		width: 32rpx;
		height: 32rpx;
	}
</style>
