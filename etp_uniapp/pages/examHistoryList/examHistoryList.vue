<!-- 考试列表-->
<template>
	<view class="content">
		<z-paging ref="paging" v-model="dataList" @query="queryExamList">

			<view class="list-box" v-for="(item,index) in dataList" :key="index" @click="itemClick(item)">
				<view
					class="k-flex-row k-flex-row-between k-margin-top-m k-margin-bottom-m k-margin-end-m k-flex-col-center">
					<view class="k-flex-row k-flex-col-center">
						<view :class="blockStyle[tranfStatus(item.status).status]"></view>
						<text class="ell-text">{{item.name?item.name:"历史考试"}}</text>
					</view>

					<text
						:class="statusStyle[tranfStatus(item.status).status]">{{tranfStatus(item.status).statusName}}</text>

				</view>
				<u-line color="#eeeeee"></u-line>

				<view class="k-flex-row">

					<view style="margin: 24rpx 10rpx 24rpx 20rpx; " v-if="item.cover">
						<u--image :src="teachCover(item.cover)" :showLoading="true" mode="aspectFill" width="230rpx"
							height="150rpx" :fade="true" radius="10rpx"></u--image>
					</view>
					<view class="k-flex-col k-flex-col-between k-margin-m">

						<text class="k-text-black k-text-md k-text-bold">总分{{item.totalScore}}分
							<text v-if="item.userScore&&item.status==9"
								class="k-margin-start-l">得分{{item.userScore}}分</text>
						</text>
						<text class="k-text-light-grey k-text-sm"
							v-if="item.status!=null&&item.status!=0">考试用时：{{secondToMinutes(item.doTime)}}</text>

						<text class="k-text-light-grey k-text-sm"
							v-if="item.status!=null&&item.status!=0&&item.status!=8">交卷方式：{{tranfHanType(item.handType)}}</text>

						<text class="k-text-light-grey k-text-sm">开始考试：{{item.startTime?item.startTime:""}}</text>
						<text class="k-text-light-grey k-text-sm"
							v-if="item.endTime&&item.status!=null&&item.status!=8">考试交卷：{{item.endTime}}</text>
						<text class="k-text-light-grey k-text-sm"
							v-if="item.logicEndTime&&item.status==0">预计结束：{{item.logicEndTime?item.logicEndTime:""}}</text>


						<text class="k-text-light-grey k-text-sm"
							v-if="item.endTime&&item.status==8">放弃考试：{{item.endTime?item.endTime:""}}</text>

					</view>

				</view>


			</view>
		</z-paging>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//v-model绑定的这个变量不要在分页请求结束中自己赋值！
				examId: '',
				dataList: [],
				keyword2: '',
				statusStyle: [
					'text-green', //正在进行中 绿色
					'text-yellow', //等待判卷  黄色
					'text-blue', //已完成 蓝色
					'text-grey', //已放弃 灰色
				],
				blockStyle: [
					'block-green', //正在进行中 绿色
					'block-yellow', //等待判卷  黄色
					'block-blue', //已完成 蓝色
					'block-grey' //已放弃 灰色
				],
				status: 0,
			}
		},

		onLoad(option) {
			this.examId = option.examId
		},

		methods: {

			search() {
				this.$refs.paging.reload();
			},

			//请求考试列表
			queryExamList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: true
				});
				let keyword = ""

				keyword = this.keyword
				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"examId": this.examId
				}
				uni.$u.api.getHistoryExamList(params).then(res => {
					uni.hideLoading();

					self.$refs.paging.completeByTotal(res.rows, res.total, true);

				}).catch(err => {
					console.log(err.errMsg);
				});




			},

			itemClick(item) {


				//判断交卷方式 handType  若为0则未交卷 随后判断试卷状态status 若为9则已完成可以查看  其他均弹窗提示
				let handType = item.handType;
				let status = item.status;

				switch (status) { //试卷状态（0未交卷 1未判卷 3未发布 9已完成） */
					case "0":
						uni.navigateTo({
							url: '/pages/exam/examine/examine?examId=' + item.examId + '&recordId=' + item
								.recordId + '&logicEndTime=' + item.logicEndTime,
							success: res => {},
							fail: () => {},
							complete: () => {}
						});
						break;
					case "1":
						uni.showToast({
							title: '考试正在判卷，请耐心等待',
							icon: 'none'
						});
						break;
					case "3":
						uni.showToast({
							title: '考试成绩尚未公布，请耐心等待',
							icon: 'none'
						});
						break;
					case "8":
						uni.showToast({
							title: '您已主动放弃本次考试',
							icon: 'none'
						});
						break;
					case "9":

						uni.navigateTo({
							url: '/pages/exam/examGrade/examGrade?examId=' + item
								.examId + "&recordId=" + item.recordId + '&logicEndTime=' + item.logicEndTime,
							success: res => {},
							fail: () => {},
							complete: () => {}
						});
						break;
					default:
						uni.showToast({
							title: '考试未出成绩，请耐心等待',
							icon: 'none'
						});
						break;
				}




			},
			//考试状态 /** 试卷状态（0未交卷 1未判卷 3未发布 9已完成） */ 当状态为未交卷时可以重新进入考试 需要特殊判断
			tranfStatus(status) {
				let statusInfo = {}
				switch (status) {
					case "0": //未交卷
						statusInfo.status = 0;
						statusInfo.statusName = "继续考试"
						return statusInfo
					case "1":
						statusInfo.status = 1;
						statusInfo.statusName = "等待判卷"
						return statusInfo

					case "3":
						statusInfo.status = 1;
						statusInfo.statusName = "等待公布"
						return statusInfo
					case "8":
						statusInfo.status = 3;
						statusInfo.statusName = "已放弃"
						return statusInfo
					case "9":
						statusInfo.status = 2;
						statusInfo.statusName = "查看成绩"
						return statusInfo

					default:
						return statusInfo;
				}
			},
			tranfHanType(type) {
				switch (type) {
					case "0":
						return "未交卷"
					case "1":
						return "人工交卷"
					case "2":
						return "强制交卷"
					default:
						return "暂无";
				}
			},
			secondToMinutes(second) {
				let minute = Math.floor(second / 60);
				let sec = second % 60;
				return `${minute}分${sec}秒`;

			}

		}
	}
</script>

<style lang="scss">
	/* 注意:父节点需要固定高度，z-paging的height:100%才会生效 */
	.content {
		background-color: #f8f8f8;
		height: 100%;
	}

	.item {
		position: relative;
		height: 150rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0rpx 30rpx;
	}


	.list-box {
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 30rpx;
		margin-top: 15px;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 24rpx 20rpx 24rpx 20rpx;
	}

	.ell-text {
		overflow: hidden;
		color: $uni-text-color;
		flex: 1;
		font-weight: bold;
		margin-left: 10px;
		font-size: 16px;
	}

	.text-normal {
		color: #333333;
		font-size: 14px;
	}

	.margin {
		margin: 30rpx 30rpx 30rpx 30rpx;
	}

	.text-green {
		color: #2FCCA7;
		font-size: 14px;
		font-weight: bold;
		flex-shrink: 0;
	}

	.text-yellow {
		color: #FEA801;
		font-size: 14px;
		font-weight: bold;
		flex-shrink: 0;
	}

	.text-blue {
		color: #037EFF;
		font-size: 14px;
		font-weight: bold;
		flex-shrink: 0;
	}

	.text-grey {
		color: #999999;
		font-size: 14px;
		font-weight: bold;
		flex-shrink: 0;
	}

	.block-green {
		background-color: #2FCCA7;
		width: 5px;
		height: 16px;
	}

	.block-yellow {
		background-color: #FEA801;
		width: 5px;
		height: 16px;
	}

	.block-blue {
		background-color: #037EFF;
		width: 5px;
		height: 16px;
	}

	.block-grey {
		background-color: #cecece;
		width: 5px;
		height: 16px;
	}
</style>
