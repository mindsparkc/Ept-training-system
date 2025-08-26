<template>
	<view class="page">
		<view class="header k-flex-row k-flex-row-between ">
			<text>本试卷共 <text class="k-text-bold k-text-lg">{{totalCount}}</text>题</text>
			<text>总分<text
					class="k-text-bold k-text-lg">{{recordInfo.totalScore?recordInfo.totalScore:'0'}}</text>分</text>
			<text>得分<text class="k-text-bold k-text-lg">{{recordInfo.userScore?recordInfo.userScore:'0'}}</text>
				分</text>

		</view>
		<view class="main">
			<kt-swiper-question ref="swiper" :list="list" :getPreviousHandler="gph" :getNextHandler="gnh"
				:dataIndex="currentIndex" @change="change" :isShowAnswer="true">
			</kt-swiper-question>
		</view>


		<view class="footer">
			<view class="flex-row" @click="openAnswerCard">
				<image src="../../../static/exam/icon-correct.png" class="small-image" style="margin-left: 30rpx;">
				</image>
				<text style="font-size: 14px;color: #037EFF;margin-left: 10rpx;">{{correctNum}}</text>
				<image src="../../../static/exam/icon-error.png" class="small-image" mode="aspectFit"
					style="margin-left: 10rpx;"></image>
				<text style="font-size: 14px;color: #f74e07;margin-left: 10rpx;" mode="aspectFit">{{errorNum}}</text>
			</view>
			<!-- 上一题 -->
			<view class="flex-row" hover-class="u-hover-class" @click="onClickLast()" style="margin-left: 40rpx;">
				<u-icon name="arrow-left" color="#303133" size="16"></u-icon>
				<text style="font-size: 32rpx;	
		color: #303133;
		font-weight: bold;">上一题</text>
			</view>

			<!-- 下一题 -->
			<view class="flex-row" hover-class="u-hover-class" @click="onClickNext()">
				<text style="font-size: 32rpx;	
		color: #303133;
		font-weight: bold;">下一题</text>
				<u-icon name="arrow-right" color="#303133" size="16"></u-icon>

			</view>
			<view class="flex-row" @click="openAnswerCard" style="margin-right: 30rpx;">
				<u-icon name="grid" color="#919199" size="28"></u-icon>
				<text class="text-bold">{{currentIndex+1}}</text>
				<text class="text-grey">/{{list.length}}</text>

			</view>

		</view>

		<!-- 答题卡弹窗 -->
		<u-popup :show="showAnswerCard" @close="close" @open="open" mode="bottom">
			<view>
				<view class="top-pop">

					<view class="flex-row">
						<image src="../../../static/exam/icon-correct.png" class="small-image"
							style="margin-left: 30rpx;">
						</image>
						<text style="font-size: 12px;color: #037EFF;margin-left: 10rpx;">{{correctNum}}</text>
						<image src="../../../static/exam/icon-error.png" class="small-image" mode="aspectFit"
							style="margin-left: 10rpx;"></image>
						<text style="font-size: 12px;color: #f74e07;margin-left: 10rpx;"
							mode="aspectFit">{{errorNum}}</text>
					</view>
					<view class="flex-row" @click="close()" style="margin-right: 30rpx;">
						<u-icon name="grid" color="#919199" size="28"></u-icon>
						<text class="text-bold">{{currentIndex+1}}</text>
						<text class="text-grey">/{{list.length}}</text>

					</view>

				</view>
				<!-- 三种状态  正确  错误 选中currentIndex==index -->
				<view class="answer-grid">
					<view v-for="(item,index) in list" :key="index" @click="onClickAnswerCardItem(index)">
						<view :class="answerCardItemState[answerCardState(index)]" class="answer-card">{{index+1}}

						</view>

					</view>
				</view>
			</view>
		</u-popup>
		<u-loading-page loading-text="试卷载入中" loading-mode="spinner" :loading="!isLoaded"></u-loading-page>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				isLoaded: false,
				recordInfo: {},
				examInfo: {},
				recordId: 0, //历史考试id
				errorNum: -1, //错误数量
				correctNum: -1, //正确数量
				totalCount: -1, //全部题目数量

				list: [],
				currentIndex: 0,
				swiperDuration: "0",
				showAnswerCard: false, //是否显示答题卡

				swiperHeight: 0,

				answerCardItemState: [
					'answer-done', //
					'answer-error', //错误
					'answer-done-select', //正确被选中
					'answer-error-select', //错误被选中

				], //答题卡状态class

			}
		},
		onLoad(option) {
			this.recordId = option.recordId;
			// this.swiperHeight = uni.getSystemInfoSync().windowHeight;
			this.requestQuestionInfo();
		},
		onReady() {

		},
		onHide() {

		},
		onUnload() {

		},
		onShow() {


		},
		onBackPress() {},
		methods: {


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


				for (let k = 0; k < allQuestion.length; k++) {
					let item = allQuestion[k];
					item.index = k;
					item.total = allQuestion.length;
				}

				//初始化数据
				this.list = allQuestion;
				this.swiperDuration = '250'

				//为题目总数  题目错误数量 题目正确数量赋值 FIXME
				let correct = 0;
				for (let j = 0; j < allQuestion.length; j++) {
					if (allQuestion[j].rightFlag) {
						correct++
					}
				}
				let allCount = allQuestion.length
				let error = allCount - correct;

				this.errorNum = error;
				this.correctNum = correct;
				this.totalCount = allCount;

			},

			requestQuestionInfo() {
				//接口请求数据 

				uni.$u.api.getHistoryExamInfo(this.recordId).then(res => {

					this.recordInfo = res.data;

					if (this.recordInfo) {

						// let allCount = this.recordInfo.quCount ? this.recordInfo.quCount : 0;
						// let correct = this.recordInfo.quCorrect ? this.recordInfo.quCorrect : 0;
						// let error = allCount - correct;
						// this.correctNum = correct;
						// this.errorNum = error;
						
						uni.setNavigationBarTitle({
							title: this.recordInfo.name
						});

						this.examInfo = this.recordInfo.exam;
						if (this.examInfo && this.examInfo.paperUnit) {
							this.formatQuestionList(this.examInfo.paperUnit)
							this.isLoaded = true;
						}

					}



				}).catch(err => {
					console.log(JSON.stringify(err));
				})

			},

			change(currentIndex) {
				this.currentIndex = currentIndex
			},
			gph: () => {
				//当前为第一个滑动触发事件
				uni.showToast({
					title: '当前为第一题',
					icon: 'none'
				});

			},
			gnh: () => {
				uni.showToast({
					title: '最后一题啦',
					icon: 'none'
				});
			},

			//下一题
			onClickNext() {
				this.$refs.swiper.goNext()
			},
			onClickLast() {
				this.$refs.swiper.goPrevious()
			},

			onClickAnswerCardItem(index) {
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


			answerCardState(index) {
				//答题卡状态 0 正确 1错误 2正确被选中 3 错误被选中 
				if (this.currentIndex == index) { //被选中
					if (this.list[index].rightFlag) { //正确被选中
						return 2
					} else { //错误被选中
						return 3
					}
				} else { //未被选中
					if (this.list[index].rightFlag) { //正确
						return 0
					} else { //错误
						return 1
					}
				}
			},




		}
	}
</script>

<style lang="scss">
	.page {
		background-color: #f8f8f8;
	}

	.header {
		height: 35px;
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		z-index: 99;
		background-color: #FFFFFF;
		text-align: center;
		line-height: 35px;
		color: #333333;
		font-size: 14px;
		padding-left: 15px;
		padding-right: 15px;

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
		padding-bottom: 8px;
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

	//答题卡状态  正确  错误  正确被选中  错误被选中DBF2FD #0058b0
	.answer-done {
		border: solid 4rpx #037EFF;
		background-color: #037EFF;
		color: #FFFFFF;
	}

	.answer-done-select {
		border: solid 4rpx #13e8ff;
		background-color: #037EFF;
		color: #FFFFFF;
	}

	.answer-error {
		border: solid 4rpx #f74e07;
		background-color: #f74e07;
		color: #FFFFFF;
	}

	.answer-error-select {
		border: solid 4rpx #af0808;
		background-color: #f74e07;
		color: #FFFFFF;
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
		font-size: 24rpx;
		font-weight: 400;
		margin-top: 20rpx;
		color: #303133;
	}

	.text-popup-tips {
		font-size: 24rpx;
		font-weight: 400;
		color: #919199;
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

	.small-image {
		width: 32rpx;
		height: 32rpx;
	}
</style>
