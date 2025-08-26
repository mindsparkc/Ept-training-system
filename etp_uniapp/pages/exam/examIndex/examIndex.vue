<template>
	<view class="page">

		<view class="relative" v-if="data&&JSON.stringify(data)!='{}'">
			<view class="bg-image">
				<image src="../../../static/exam/icon-exam-title.png" class="image-top"></image>
			</view>
			<view class="white-box">
				<view class="text-exam-title">{{data.name}}</view>
				<view class="flex-row">
					<text class="text-title">考生姓名</text>
					<text class="text-content text-bold">{{userName}}
					</text>
				</view>
				<view class="flex-row">
					<text class="text-title">考试时长</text>
					<text class="text-content"><text class="text-bold">{{data.totalTime&&data.totalTime!=0?data.totalTime+' 分钟':'不限时长'}}</text><text
							class="k-margin-start-m" v-if="data.timeLimit==1">允许迟到{{data.lateTime}}分钟</text></text>
				</view>
				<view class="flex-row">
					<text class="text-title">考试总分</text>
					<text class="text-content">
						<text class="text-bold">{{data.totalScore}}</text>
						分&emsp;{{data.qualifyScore?data.qualifyScore+'分及格':''}}</text>
				</view>
				<view class="flex-row">
					<text class="text-title">考试次数</text>

					<text class="text-content">已考<text class="text-bold">{{data.usedChance}}</text>
						{{data.chance == 0?'次&emsp;总次数不限':'次&emsp;共计'+data.chance+'次'}}</text>
					<text></text>
				</view>
				<view class="flex-row">
					<text class="text-title">开始时间</text>
					<!-- # 考试发布时间（若不限制考试开始时间，则考试发布即能开始考试） -->
					<text
						class="text-content">{{data.timeLimit==1? $u.timeFormat(formatTime(data.startTime), 'yyyy-mm-dd hh:MM:ss') :$u.timeFormat(formatTime(data.publishTime), 'yyyy-mm-dd hh:MM:ss')}}
						<text class="k-text-sm " style="color: #F0AD4E;"
							v-if="data.timeLimit==1">\n允许迟到{{data.lateTime}}分钟 过时将不可参加考试</text> </text>
				</view>
				<view class="flex-row" v-if="data.endTime">
					<text class="text-title">结束时间</text>
					<!-- # 考试发布时间（若不限制考试开始时间，则考试发布即能开始考试） -->
					<text class="text-content">
						{{ data.endTime? $u.timeFormat(formatTime(data.endTime), 'yyyy-mm-dd hh:MM:ss'):''}}</text>
				</view>

				<view class="flex-row">

					<text class="text-title">考试题型</text>
					<text class="text-content">{{examQuestionsDes(data.paperUnit)}}</text>
				</view>
				<!-- <view class="flex-row">
					
					<text class="text-title">分数等级</text>
					<view class="text-content">{{ examQuestionCount(data.fsdj)}}</view>
				</view> -->
				<!-- <view class="flex-row">
					
					<text class="text-title">组卷策略</text>
					<text class="text-content">{{data.zjcl}}</text>
				</view> -->


				<view class="flex-row">
					<text class="text-title">考试描述</text>
					<text class="text-content">{{data.content?data.content:'暂无'}}</text>
				</view>
				<view class="flex-row">
					<text class="text-title">注意事项</text>
					<text class="text-content">{{makeTips()}}</text>
				</view>

				<view class="flex-row" v-if="false">

					<text class="text-title">考试细则</text>
					<text class="text-underline" @click="catRule=true;showRulePopup=true;">查看考试细则</text>

				</view>
				<view class="k-flex-row k-flex-col-center k-margin-top-l" v-if="data&&data.openType&&data.openType==5">

					<text class="text-title">考试口令</text>
					<view class="k-margin-start-xl">
						<u-input placeholder="请输入口令密码" v-model="password" clearable></u-input>
					</view>


				</view>
				<view class="k-margin-top-l " v-if="records&&records.length>0&&status==1">

					<u-swipe-action v-if="true">
						<u-swipe-action-item v-for="(item,index) in records" :key="item.recordId" :index="item.recordId"
							:options="options" :name="item.recordId" @click="deleteRecordExam">

							<view class="k-flex-row k-flex-row-between"
								style="background-color: #FFFFFF;border-radius: 5px; border:1px solid rgba(0, 0, 0, 0.1);padding: 6px;"
								@click="goOnExam(item.recordId)" hover-class="u-hover-class">
								<view class="k-flex-row">
									<image src="../../../static/exam/icon-time.png" style="width:55px;height:55px"
										class="k-flex-shrink">
									</image>
									<view class="k-flex-col k-flex-col-between k-margin-start-m">

										<text class="k-text-md k-text-bold k-warning">正在进行中的考试</text>
										<text
											class="k-margin-top-sm k-text-sm k-text-black">考试时间：{{item.startTime}}</text>
									</view>
								</view>
								<view class="k-flex-row">
									<view class="k-flex-row k-flex-col-center">
										<uni-icons type="forward" color="#999999" size="30"></uni-icons>
									</view>


									<view hover-class="u-hover-class" style="height: 30px;width: 30px;;"
										class="k-flex-row k-flex-col-top k-flex-row-center"
										@click.stop="deleteRecordExam({'name':item.recordId,'index':index})">
										<uni-icons type="close" color="#E91A1B" size="20"></uni-icons>
									</view>
								</view>

							</view>
						</u-swipe-action-item>
					</u-swipe-action>




				</view>
				<!-- <view class="flex-row">
					
					<text class="text-title">考试标签</text>
					<view v-for="(item,index) in splitTags(data.tags)" :key="index" class="text-content">
						<text class="tag-blue">{{item}}</text>
					</view>

				</view> -->



			</view>

			<view class="flex-row-between">
				<view class="btn-second" hover-class="u-hover-class" @click="catHistory()">查看历史</view>

				<view class="btn-time-normal">
					<view class="text-time-tips" >
						<text>{{timeText}}{{time}}</text>
					</view>
					<view :class="btnStyle[status]" class="btn-normal" hover-class="u-hover-class" @click="examBegin()">
						{{btnText}}
					</view>
				</view>

			</view>
		</view>

		<u-popup :show="showCountDownPopup" mode="center" round="20" :customStyle="rulesStyle">
			<view class="flex-row-center">
				<text class="text-popup-subtitle">距离考试开始仅剩</text>
				<text class="text-popup-title-red">{{time}}</text>

				<image src="../../../static/exam/icon-exam-undone.png" class="img-done"></image>
				<text class="text-popup-tips">考试尚未开始 请耐心等待</text>

				<view class="flex-row-between">
					<view class="btn-popup-normal" hover-class="u-hover-class" @click="showCountDownPopup=false">取消
					</view>
					<view class="btn-popup-primary" hover-class="u-hover-class" @click="showCountDownPopup=false">确定
					</view>
				</view>
				<text class="text-popup-tips">文明考风 诚信考试</text>
			</view>
		</u-popup>
		<!-- 考试细则弹窗 -->
		<u-popup :show="showRulePopup" mode="center" round="20" :customStyle="rulesStyle">
			<view class="flex-row-center">
				<text class="k-text-black k-text-bold k-text-xl">考试细则</text>
				<text class="text-popup-tips k-margin-top-m">请仔细阅读并严格遵守下方考试细则</text>
				<scroll-view style="height: 200px;margin-top: 5px;" scroll-y show-scrollbar>
					<text
						style="">请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则请仔细阅读下列考试细则</text>

				</scroll-view>
				<view class="btn-primary-popup" hover-class="u-hover-class" @click="goToExam()">
					我已认真阅读并承诺严格遵守考试规定
				</view>
				<view class="btn-normal-popup" hover-class="u-hover-class" @click="showRulePopup=false">取消
				</view>
				<text class="text-popup-tips k-margin-top-m">文明考风 诚信考试</text>

			</view>
		</u-popup>

	</view>

</template>


<script>
	import timer from '../../../common/timer.js'
	export default {
		data() {
			return {
				password: "", //考试口令 当openType为5时使用口令
				userName: "",
				showCountDownPopup: false,
				examId: 0, //考试ID
				data: {},
				btnText: "开始考试",
				timeText: "距考试截止", //距考试开始还剩
				status: 0, //考试状态 0未开始  1进行中 2结束
				time: '', //倒计时显示时间
				countDownSeconds: 0,
				btnStyle: [
					'btn-disabled', //未开始
					'btn-primary', //进行中
					'btn-disabled-grey', //已结束
					'btn-disabled-grey' //无答题次数
				],
				showRulePopup: false, //考试规则弹窗
				catRule: false, //仅查看考试细则 点击按钮不会跳转开始考试
				rulesStyle: {
					width: '80%',
				},
				records: [], //考试记录
				options: [{
					text: '放弃',
					style: {
						backgroundColor: '#f56c6c'
					}
				}],
				isRecordShow: false, //是否显示正在进行中的考试

			}
		},
		onLoad(option) {
			let userInfo = uni.getStorageSync("userInfo");
			this.userName = userInfo.nickName;
			this.examId = option.examId;
		},
		onShow() {
			this.getExamination();
		},
		onUnload() {
			timer.clear()
		},

		methods: {
			countDown() {
				if (this.countDownSeconds != 0) {
					timer.countdown({
						id: 'test',
						seconds: this.countDownSeconds,
						change: res => {
							let day = res.days != 0 ? res.days + '天' : '';
							let hour = res.hours;
							let minutes = res.minutes;
							let seconds = res.seconds;
							let time = day + hour + ':' + minutes + ':' + seconds;


							this.time = time;
						},
						done: () => {
							if (this.status == 0) {
								this.status = 1
							} else if (this.status == 1) {
								this.status = 2
							}

						}
					});
				}

			},


			getExamination() {
				uni.showLoading({
					title: '加载中...',
					mask: false
				});
				uni.$u.api.getExamInfo(this.examId).then(res => {
					uni.hideLoading()
					this.data = res.data;
					this.records = this.data.records;
					if (this.data && this.data.name) {
						uni.setNavigationBarTitle({
							title: this.data.name
						});
					}
					this.judgeExam();


				}).catch(err => {
					uni.hideLoading()
					console.log(err);
				})
			},

			parseJson(jsonStr) {
				if (jsonStr) {
					return JSON.parse(jsonStr)
				} else {
					return []
				}
			},

			// 试题题型转换
			tranfQuestionType(type) {
				let key = "";
				switch (type) {
					case "1":
						key = "单选";
						break;
					case "2":
						key = "多选";
						break;
					case "3":
						key = "判断";
						break;
					case "4":
						key = "填空";
						break;
					case "5":
						key = "问答";
						break;
					case "6":
						key = "组合";
						break;
					default:
						key = "其他";
						break;
				}
				return key;
			},

			//考试题目数量详情 考试题型
			examQuestionsDes(des) {
				//完形填空（填空 共5题 总计10分） 
				let querstionDesc = "";
				for (let i = 0; i < des.length; i++) {
					let typeName = this.tranfQuestionType(des[i].quType)
					let desc = des[i].display + " (共" + des[i].quCount + "题 共"+des[i].totalScore+"分)\n";
					querstionDesc = querstionDesc.concat(desc)
				}


				return querstionDesc;
			},
			//开始考试
			examBegin() {
				switch (this.status) {
					case 0: //考试未开始
						//弹窗提示时间
						if (!this.showCountDownPopup) {
							this.showCountDownPopup = true;
						}
						break;
					case 1: //开始考试
						//若需要输入口令密码的考试  提示输入口令密码
						let openType = this.data.openType;
						if (openType && openType == 5) {
							if (this.password) {

								this.goToExamChargeRecord()
								// this.catRule = false; //我已阅读按钮可跳转到考试页面
								// this.showRulePopup = true; //展示考试细则弹窗

							} else {
								uni.showToast({
									title: '请输入考试口令密码',
									icon: 'none'
								});
							}
						} else {
							// this.catRule = false; //我已阅读按钮可跳转到考试页面
							// this.showRulePopup = true; //展示考试细则弹窗
							this.goToExamChargeRecord()
						}



						break;
					case 2: //考试已经结束
						uni.showToast({
							title: '考试已结束',
							icon: 'none'
						});
						break;
					case 3: //考试次数用尽
						uni.showToast({
							title: '考试次数已用尽',
							icon: 'none'
						});
						break;

					default:
						break;
				}

			},
			//跳转考试页面
			goToExam() {
				this.showRulePopup = false;
				if (!this.catRule) {
					uni.navigateTo({
						url: '/pages/exam/examine/examine?examId=' + this.examId + '&password=' + this.password,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				}
			},
			//开始考试判断是否有考试记录 若有考试记录则提示
			goToExamChargeRecord() {
				if (this.records && this.records.length > 0) {
					let recordId = this.records[0].recordId; //取最新的一条
					uni.showModal({
						title: '温馨提示',
						content: '您有正在进行中的考试。\n考试时间：' + this.records[0].startTime +
							'。\n放弃正在进行的考试将会消耗掉一次考试机会，若考试机会用尽则不可再开始新的考试，请您慎重选择。\n',
						showCancel: true,
						cancelText: '放弃重考',
						confirmText: '继续考试',
						success: res => {
							if (res.confirm) {
								uni.navigateTo({
									url: '/pages/exam/examine/examine?examId=' + this.examId +
										'&password=' + this.password + '&recordId=' + recordId +
										'&logicEndTime=' + this.records[0].logicEndTime,
									success: res => {},
									fail: () => {},
									complete: () => {}
								});
							} else { //放弃本场考试重新考试
								//删除单条正在进行的考试记录
								uni.showLoading({
									title: '请稍候...',
									mask: false
								});
								uni.$u.api.giveupExam(recordId).then(res => {
									uni.hideLoading()
									//删除成功
									this.records.splice(0, 1);
									uni.navigateTo({
										url: '/pages/exam/examine/examine?examId=' + this
											.examId + '&password=' + this.password,
										success: res => {},
										fail: () => {},
										complete: () => {}
									});

								}).catch(err => {
									uni.hideLoading()
									uni.showToast({
										title: '删除考试记录失败',
										icon: 'none'
									});

								})

							}
						},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.navigateTo({
						url: '/pages/exam/examine/examine?examId=' + this.examId + '&password=' + this.password,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				}
			},

			//对能否开始考试进行判断 若限制考试开始时间 则
			judgeExam() {
				//判断考试是否进行中 
				this.judgeDate();
			},
			judgeDate() {
				let status = this.data.status;
				let obj = {}
				if (status == 3 || status == 4) { //强制结束
					this.btnText = "考试已结束";
					this.timeText = ""
					this.status = 2;
					this.countDownSeconds = 0;
				} else if (status == 2) { //已发布
					//若限制考试开始时间 则startTime为开始时间 否则为发布时间
					let startTime = this.data.timeLimit == 1 ? this.formatTime(this.data.startTime) : this.formatTime(this
						.data.publishTime);
						
				
					let now = new Date().getTime();

					if (now < startTime) { //当前时间小于开始时间 则考试未开始

						this.btnText = "考试尚未开始";
						this.timeText = "距考试开始"
						this.status = 0;
						this.countDownSeconds = (startTime - now) / 1000;

					} else { //当前时间大于开始时间
						let endTime = this.formatTime(this.data.endTime);
						let end = this.data.endTime;
						if (end) {
							if (now < endTime) { //当前时间小于结束时间 考试进行中
								//判断考生的答题次数  若没有答题次数则显示
								let totalCount = this.data.chance;
								let userCount = this.data.usedChance;
							
								if (totalCount != 0 && (totalCount - userCount) <= 0) {
									this.btnText = "考试次数已用尽";
									this.timeText = ""
									this.status = 3;
									this.countDownSeconds = 0;
								} else {
									this.btnText = "开始考试";
									this.timeText = "距考试截止"
									this.status = 1;
									this.countDownSeconds = (endTime - now) / 1000;
								}
							
							} else { //当前时间大于结束时间  考试已结束
								this.btnText = "考试已结束";
								this.timeText = ""
								this.status = 2;
								this.countDownSeconds = 0;
							}
						}else{
								this.btnText = "开始考试";
								this.status = 1;
								this.timeText = "";
								this.countDownSeconds = 0;
								
						}
						
					
					
						
					}
				}

				this.countDown()


			},
			catHistory() {
				uni.navigateTo({
					url: '/pages/examHistoryList/examHistoryList?examId=' + this.data.examId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			//返回时间戳
			formatTime(time) {
				return (new Date(time)).getTime();
			},
			makeTips() {
				let gradeTips = "";
				//成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
				let showScore = this.data.showScore
				switch (showScore) {
					case "0":
						gradeTips = "本次考试不公布成绩。"
						break;
					case "1":
						gradeTips = "本次考试结束后将立即公布成绩。"
						break;
					case "2":
						gradeTips = "本次考试将于试卷批改完成之后公布成绩。"
						break;
					case "3":
						gradeTips = "本次考试将于" + this.data.showScoreTime + "之后公布成绩。"
						break;
					case "4":
						gradeTips = "本次考试将由管理员公布成绩。"
						break;
					default:
						break;
				}

				//提醒允许切屏次数  提醒无操作时间 提醒成绩显示策略
				let handMinTime = this.data.handMinTime != 0 ? "考试开始" + this.data.handMinTime + "分钟前不允许交卷，" : '';
				let leaveLimit = this.data.leaveLimit != 0 ? '考试期间切屏次数达到' + this.data.leaveLimit + '次将被判为作弊。' : '';
				let noActLimit = this.data.noActLimit != 0 ? this.data.noActLimit + '分钟内无操作系统将自动为您交卷。' : '';

				// let tips = handMinTime + "考试期间切屏次数达到" + this.data.leaveLimit +
				// 	"次将被判为作弊，" + this.data.noActLimit +
				// 	"分钟内无操作系统将自动为您交卷。" + gradeTips;
				let tips = handMinTime + leaveLimit + noActLimit + gradeTips;
				return tips;
			},
			deleteRecordExam(e) {
				uni.showModal({
					title: '温馨提示',
					content: '确定要放弃本场考试吗',
					success: res => {
						if (res.confirm) {
							uni.showLoading({
								title: '正在删除...',
								mask: false
							});
							//删除单条正在进行的考试记录
							uni.$u.api.giveupExam(e.name).then(res => {
								uni.hideLoading()
								uni.showToast({
									title: '已放弃本次考试记录',
									icon: 'none'
								});
								//删除成功
								this.records.splice(e.index, 1);

							}).catch(err => {
								uni.hideLoading()
								console.log(err);
							})

						}
					}
				})


			},
			goOnExam(recordId) {
				// //继续考试
				uni.navigateTo({
					url: '/pages/exam/examine/examine?examId=' + this.examId + '&password=' + this.password +
						'&recordId=' + recordId + '&logicEndTime=' + this.records[0].logicEndTime,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});


			}




		}
	}
</script>

<style>
	.page {
		background-color: #FFFFFF;
		padding-top: 66rpx;
		height: 100%;
	}

	.white-box {
		background-color: #FFFFFF;
		padding: 40rpx 40rpx 50rpx 40rpx;
		/* box-shadow: 1px 6px 9px 1px rgba(103, 116, 130, 0.1); */
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		border-radius: 10px;
		margin: 26rpx 30rpx 10rpx 30rpx;
		display: flex;
		flex-direction: column;
	}

	.btn-margin {
		margin-top: 40rpx;
		margin-left: 84rpx;
		margin-right: 84rpx;
	}

	.image-top {
		width: 60rpx;
		height: 60rpx;

	}

	.relative {
		position: relative;
	}

	.bg-image {
		border-radius: 50%;
		background-color: #E1ECF6;
		width: 110rpx;
		height: 110rpx;
		display: flex;
		justify-content: center;
		align-items: center;

		position: absolute;
		/* left: 50%; */
		/* transform: translate(-50%, -50%);   */

		left: 0;
		right: 0;
		top: -5%;
		margin: auto;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

	}

	.text-exam-title {
		color: #303133;
		font-weight: bold;
		margin-top: 20rpx;
		font-size: 36rpx;
		text-align: center;
	}

	.image-small {
		width: 30rpx;
		height: 30rpx;
		margin-right: 10rpx;
		flex-shrink: 0;
	}

	.margin-top-20 {
		margin-top: 40rpx;

	}

	.text {
		margin-left: 10rpx;
	}

	.text-title {
		color: #999999;
		font-size: 32rpx;
		flex-shrink: 0;
	}

	.text-content {
		color: #303133;
		font-size: 32rpx;
		padding-left: 40rpx;
		/* font-weight: bold; */
	}

	.text-bold {
		font-weight: bold;
		padding-right: 5rpx;
	}

	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);

		padding: 6rpx 10rpx 6rpx 10rpx;
		color: #007DFF;
		font-size: 28rpx;
		border-radius: 4rpx;
		margin-right: 8rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		margin-top: 10px;
		justify-content: flex-start;
		align-items: flex-start;

	}

	.flex-row-between {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: flex-end;
		margin-top: 30rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 60rpx;
	}

	.btn-normal {
		border-radius: 10rpx;
		padding: 24rpx;
		font-size: 28rpx;
		text-align: center;
	}



	.btn-primary {
		background-color: #037EFF;
		color: #FFFFFF;
	}

	.btn-disabled {
		background-color: #037EFF;
		color: #EEEEEE;
		opacity: 0.5;
	}

	.btn-disabled-grey {
		background-color: #EEEEEE;
		color: #909193;
	}

	.btn-time-normal {
		flex: 1;
		text-align: center;
		margin-left: 20rpx;
	}

	.btn-second {
		flex: 1;
		border-radius: 10rpx;
		padding-top: 24rpx;
		padding-bottom: 24rpx;
		margin-right: 20rpx;
		font-size: 28rpx;
		text-align: center;
		/* background-color: #d8e9f9; */
		background-color: #40ba8d;
		/* border: solid #037EFF 1rpx; */
		/* margin-left: 30rpx; */
		/* margin-right: 20rpx; */
		color: #FFFFFF;
	}

	.text-time-tips {
		color: #F0AD4E;
		font-size: 28rpx;
	}

	.flex-row-center {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx;
		margin: 30rpx;
	}

	.img-done {
		width: 300rpx;
		height: 200rpx;
		margin-top: 24rpx;
		margin-bottom: 24rpx;
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
	}

	.text-popup-subtitle {
		font-size: 28rpx;
		font-weight: 400;
		margin-bottom: 20rpx;
		color: #303133;
	}

	.text-popup-tips {
		font-size: 24rpx;
		font-weight: 400;
		color: #919199;
	}


	.btn-popup-primary {
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

	.btn-popup-normal {
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

	.btn-primary-popup {
		border-radius: 10rpx;
		background-color: #037EFF;
		color: white;
		width: 100%;
		text-align: center;
		font-size: 28rpx;
		padding: 20rpx;
		margin-top: 12px;
	}

	.btn-normal-popup {
		border-radius: 10rpx;
		background-color: rgba(0, 125, 255, 0.1);
		color: #037EFF;
		width: 100%;
		text-align: center;
		font-size: 28rpx;
		padding: 20rpx;
		margin-top: 12px;
	}

	.text-underline {
		text-decoration: underline;
		color: #037EFF;
		font-size: 32rpx;
		padding-left: 40rpx;
	}
</style>
