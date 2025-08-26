<template>
	<!-- 考试成绩 -->
	<view>

		<!-- #ifndef H5 -->
		<view class="bg-title">

			<view @click="back">
				<uni-icons type="left" color="#fff" size="30"></uni-icons>
			</view>
		</view>
		<!-- #endif -->


		<view class="bg-top">
			<view style="display: flex;justify-content: center;padding-top: 40rpx;">
				<circle-progress cid="circle37" type="dashboard"
					:percent="getPercent(examGrade.userScore,examGrade.totalScore)" font-color="#ffffff" :font-size="20"
					:width="100" stroke-color="#ffffff" stroke-background="#0368D1" :text="examGrade.userScore">
				</circle-progress>
			</view>


			<view class="flex-row-between absoult">
				<text class="text-white-normal" style="margin-left: 30rpx;">试卷总分：
					<text class="text-white-bold">{{examGrade.totalScore?examGrade.totalScore:'0'}}分</text>
				</text>
				<text class="text-white-normal">正确率：
					<text class="text-white-bold"
						style="margin-right: 30rpx;">{{getPercent(examGrade.correctNum,examGrade.totalNum)}}%</text>
				</text>
			</view>
			<image
				:src="waveGif"
				mode='scaleToFill' class='gif-wave'></image>
		</view>

		<view v-for="(item,index) in examGradeInfo " :key="index">
			<view class="bg-card">
				<view class="bg-card-top text-blue-small">{{item.typeName}}</view>
				<view class="bg-card-flex-row">
					<text class="text-small">正确
						<text class="text-green">{{item.correctNum}}</text>

					</text>
					<text class="text-small">错误
						<text class="text-red">{{item.totalNum-item.correctNum}}</text>
					</text>
					<view class="flex-row">
						<text class="text-small text-small-padding">错误率
						</text>

						<u-line-progress :percentage="getPercent(item.totalNum-item.correctNum,item.totalNum)"
							activeColor="#037EFF" inactiveColor="rgba(3, 126, 255, 0.2)" :showText="false">
						</u-line-progress>
						<text class="text-blue">{{getPercent(item.totalNum-item.correctNum,item.totalNum)}}%</text>
					</view>


				</view>

			</view>

		</view>

		<view class="flex-row-between">
			<view class="btn-normal" hover-class="u-hover-class" @click="catPaper()">查看试卷</view>
			<view class="btn-primary" hover-class="u-hover-class" @click="examAgain()">再考一次</view>
		</view>



	</view>
</template>

<script>
	export default {

		data() {
			return {
				recordId: "",
				examId: 0, //考试ID
				//总体得分情况
				examGrade: {},
				//各个类型题目得分情况
				examGradeInfo: [],
				waveGif:this.$constant.WAVE,

			}
		},
		onLoad(option) {
			this.recordId = option.recordId; //考试记录的id
			this.examId = option.examId;
			this.getGrade();

		},
		methods: {
			//获取考试成绩
			getGrade() {
				uni.showLoading({
					title: '请稍候...',
					mask: false
				});
				uni.$u.api.getHistoryExamInfo(this.recordId).then(res => {
					this.getGradeInfo(res.data)
					uni.hideLoading()
				}).catch(err => {
					uni.hideLoading()
					console.log(err.errMsg);
				})
			},
			/**
			 * 求百分比
			 * @param  num 当前数
			 * @param  total 总数
			 */
			getPercent(num, total) {
				num = parseFloat(num);
				total = parseFloat(total);
				if (isNaN(num) || isNaN(total)) {
					return 0;
				}
				return total <= 0 ? 0 : Math.round((num / total) * 10000) / 100.0;
			},
			//计算各个题型的正确错误数量
			getGradeInfo(data) {
				//将题目从单元中拆分出来形成题目列表
				let questionList = []
				let val = data.exam.paperUnit;
				for (let i = 0; i < val.length; i++) {
					questionList = questionList.concat(val[i].questions)
				}
				// console.log(JSON.stringify(questionList));
				//根据题目列表统计总数 正确题目数
				this.examGrade.totalNum = questionList.length;
				let correctNum = 0;
				for (let k = 0; k < questionList.length; k++) {
					if (questionList[k].rightFlag) {
						correctNum++
					}
				}

				//从返回值中取出总得分以及用户得分
				this.examGrade.correctNum = correctNum;
				this.examGrade.totalScore = data.exam.totalScore;
				this.examGrade.userScore = data.exam.userScore;

				// console.log(JSON.stringify(this.examGrade));

				//按照类型分类 统计每个类型中的正确题目数量
				let typeQuestion = []
				let groupData = new Map()
				questionList.forEach(item => {

					if (!groupData.has(item.type)) {
						groupData.set(item.type, [item])
					} else {
						groupData.get(item.type).push(item)
					}
				})


				//遍历map 将每个type对应的value数组 总数量 正确数量统计出来
				groupData.forEach((value, key) => {
					let grade = {}
					grade.type = key;
					grade.typeName = this.transfType(key)

					grade.totalNum = value.length;
					let correctNum2 = 0;
					for (let i = 0; i < value.length; i++) {
						if (value[i].rightFlag) {
							correctNum2++
						}

					}
					grade.correctNum = correctNum2;

					this.examGradeInfo.push(grade);
				})

			},
			transfType(type) {
				switch (type) {
					case "1":
						return "单选题";
					case "2":
						return "多选题";
					case "3":
						return "判断题";
					case "4":
						return "填空题";
					case "5":
						return "问答题";
					case "6":
						return "组合题";
					default:
						break;
				}
			},

			back() {
				uni.navigateBack({
					delta: 1
				});
			},
			//查看试卷
			catPaper() {
				let errorNum = this.examGrade.totalNum - this.examGrade.correctNum

				uni.redirectTo({
					url: '/pages/exam/catExam/catExam?recordId=' + this.recordId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			examAgain() {
				uni.redirectTo({
					url: '/pages/exam/examIndex/examIndex?examId=' + this.examId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}

		}

	}
</script>

<style>
	.bg-title {
		background-color: #3196ff;
		color: #FFFFFF;
		display: flex;
		position: relative;
		flex-direction: row;
		align-items: center;
		padding-left: 30rpx;
		padding-top: 100rpx;
		font-size: 32rpx;
		color: #FFFFFF;
	}

	.item {
		margin-top: 40upx;
		padding: 0 20upx;
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		justify-content: space-between;
	}

	.name {
		text-align: center;
		margin-top: -10upx;
		font-size: 28rpx;
		font-weight: 400;
		color: #FFFFFF;
	}


	.bg-top {
		background: linear-gradient(#3196ff, #bcc7fe);
		height: auto;
		z-index: -1;
		position: relative;
		min-height: 180px;
	}

	.gif-wave {
		position: absolute;
		width: 100%;
		bottom: 0;
		left: 0;
		z-index: 99;
		mix-blend-mode: screen;
		height: 100rpx;
	}

	.flex-row-between {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		margin-top: 30rpx;
	}

	.absoult {
		position: absolute;
		bottom: 40rpx;
		width: 100%;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		align-items: center;
		flex: 0.5;

	}

	.text-white-normal {
		color: white;
		font-size: 32rpx;

	}

	.text-white-bold {
		color: white;
		font-size: 40rpx;
		font-weight: bold;

	}

	.bg-card {
		background-color: white;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
		margin: 30rpx;
		border-radius: 4rpx;
		padding-bottom: 40rpx;
	}

	.bg-card-top {
		border-top-left-radius: 4rpx;
		border-top-right-radius: 4rpx;
		background-color: rgba(3, 126, 255, 0.1);
		padding: 10rpx 26rpx;
	}

	.bg-card-flex-row {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		margin-top: 40rpx;

	}

	.text-red {
		color: #DC2323;
		font-weight: bold;
		font-size: 28rpx;
		margin-left: 24rpx;
	}

	.text-green {
		color: #15A957;
		font-weight: bold;
		font-size: 28rpx;
		margin-left: 24rpx;
	}

	.text-blue {
		color: #037EFF;
		font-weight: bold;
		font-size: 28rpx;
		margin-left: 10rpx;
		width: 60rpx;
		flex-shrink: 1;
	}

	.text-small {
		color: #303133;
		font-size: 24rpx;
	}

	.text-small-padding {
		padding-right: 10rpx;
	}

	.text-blue-small {
		color: #037EFF;
		font-size: 24rpx;
	}


	.btn-primary {
		border-radius: 10rpx;
		background-color: #037EFF;
		color: white;
		flex: 1;
		margin-top: 30rpx;
		margin-bottom: 60rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		padding: 24rpx;
		font-size: 28rpx;
		text-align: center;
	}

	.btn-normal {
		flex: 1;
		border-radius: 10rpx;
		background-color: rgba(0, 125, 255, 0.1);
		padding: 24rpx;
		color: #037EFF;
		margin-top: 30rpx;
		margin-right: 30rpx;
		margin-left: 30rpx;
		margin-bottom: 60rpx;
		font-size: 28rpx;
		text-align: center;

	}
</style>
