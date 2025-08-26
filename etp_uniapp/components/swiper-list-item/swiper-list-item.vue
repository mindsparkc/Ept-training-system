<!-- 考试列表-->
<template>
	<view class="content">
		<!-- 这里设置了z-paging加载时禁止自动调用reload方法，自行控制何时reload（懒加载）-->
		<!--  :enable-back-to-top="currentIndex===tabIndex" 在微信小程序上可以多加这一句，因为默认是允许点击返回顶部的，但是这个页面有多个scroll-view，会全部返回顶部，所以需要控制是当前index才允许点击返回顶部 -->
		<z-paging ref="paging" v-model="dataList" @query="queryExamList" :fixed="false" :auto="false"
			:enable-back-to-top="currentIndex===tabIndex">
			<view class="margin">
				<u-search placeholder="请输入考试名称" v-model="keyword1" bgColor="#ffffff" :show-action="true" actionText="搜索"
					:animation="true" @custom="search" @search="search" v-if="tabIndex==0" :clearable="false">
				</u-search>
				<u-search placeholder="请输入考试名称" v-model="keyword2" bgColor="#ffffff" :show-action="true" actionText="搜索"
					:animation="true" @custom="search" @search="search" v-if="tabIndex==1" :clearable="false">
				</u-search>
			</view>

			<view class="list-box" v-for="(item,index) in dataList" :key="index" @click="itemClick(item)">
				<view style="display: flex;flex-direction: row;">
					<text class="ell-text">{{item.mc?item.mc:item.ksmc}}</text>
					<text class="cover-blue" v-if="tabIndex==0">限时{{item.djsc}}'</text>
					<text :class="[item.score?'cover-green':'cover-red']"
						v-if="tabIndex==1">{{item.score?judgeLevel(item.fsdj):'未完成'}}</text>

				</view>
				<view style="display: flex;flex-direction: row;">

					<view style="margin: 24rpx 20rpx 24rpx 20rpx;">
						<u--image :src="teachCover(item.fm)" :showLoading="true" mode="aspectFill" width="240rpx"
							height="140rpx" :fade="true" radius="10rpx"></u--image>
					</view>
					<view class="flex-col" v-if="tabIndex==1">
						<text class="text-small-grey" v-if="item.score">交卷时间：{{item.updateTime}}</text>
						<text class="text-small-grey" v-else>考试时间:{{item.createTime?item.createTime:""}}</text>

						<text class="text-small-grey">限时{{item.djsc}}分钟</text>
						<u-button type="primary" shape="circle" text="查看成绩" size="mini" v-if="item.score"></u-button>

					</view>
					<view class="flex-col" v-if="tabIndex==0">
						<text class="text-small-grey">开始时间：{{item.kskssj}}</text>
						<text class="text-small-grey">截止时间：{{item.ksjssj}}</text>
						<u-button type="primary" shape="circle" text="进入考试" size="mini"></u-button>

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
				dataList: [],
				firstLoaded: false,
				keyword1: '',
				keyword2: '',
			}
		},
		props: {
			//当前组件的index，也就是当前组件是swiper中的第几个
			tabIndex: {
				type: String,
				default: function() {
					return "0"
				}
			},
			//当前swiper切换到第几个index
			currentIndex: {
				type: Number,
				default: function() {
					return 0
				}
			}

		},
		watch: {
			currentIndex: {
				handler(newVal) {
					if (newVal == this.tabIndex) {
						//懒加载，当滑动到当前的item时，才去加载
						if (!this.firstLoaded) {
							console.log("是否加载过数据>>" + this.firstLoaded);
							setTimeout(() => {
								this.$refs.paging.reload();
							}, 5);
						}
					}
				},
				immediate: true
			},
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
					mask: false
				});
				let keyword = ""
				if (this.tabIndex == 0) { //当前考试
					keyword = this.keyword1
					let params = {
						"pageNum": pageNo,
						"pageSize": pageSize,
						"mc": keyword,
					}
					uni.$u.api.getExamList(params).then(res => {
						uni.hideLoading();
						this.firstLoaded = true;

						self.$refs.paging.completeByTotal(res.rows, res.total, true);
					}).catch(err => {
						console.log(err.errMsg);
					});

				}
				if (this.tabIndex == 1) { //历史考试
					keyword = this.keyword2
					let params = {
						"pageNum": pageNo,
						"pageSize": pageSize,
						"ksmc": keyword,
					}
					uni.$u.api.getHistoryExamList(params).then(res => {
						uni.hideLoading();
						this.firstLoaded = true;

						self.$refs.paging.complete(res.rows);

					}).catch(err => {
						console.log(err.errMsg);
					});

				}


			},

			itemClick(item) {
				if (this.tabIndex == 0) {
					uni.navigateTo({
						url: '/pages/exam/examIndex/examIndex?examId=' + item.id + "&paperId=" + item.sjid +
							"&examTitle=" + item.mc + "&time=" + item.djsc,
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					//若未完成考试 则提示不可点击进入查看成绩
					if (!item.score) {
						uni.showToast({
							title: '本次考试未完成',
							icon: 'none'
						});

					} else {
						uni.navigateTo({
							url: '/pages/exam/examGrade/examGrade?examId=' + item.ksid + "&id=" + item.id,
							success: res => {},
							fail: () => {},
							complete: () => {}
						});
					}


				}

			},
			//考试等级
			judgeLevel(level) {
				if (level) {
					return level
				} else {
					return '未评级'
				}
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

	.cover-blue {
		background-color: rgba(0, 125, 255, 0.2);
		// width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.cover-red {
		background-color: rgba(221, 11, 11, 0.2);
		// width: 120rpx;
		height: 40rpx;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #DD0B0B;
		font-size: $uni-font-size-ssm;
	}

	.cover-green {
		background-color: rgba(17, 188, 77, 0.2);
		width: 80rpx;
		height: 35rpx;
		text-align: center;
		border-bottom-left-radius: 30rpx;
		border-top-right-radius: 10rpx;
		padding: 5rpx 16rpx 5rpx 16rpx;
		color: #11BC4D;
		font-size: $uni-font-size-ssm;
	}

	.list-box {
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 30rpx;

	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 24rpx 20rpx 24rpx 20rpx;
	}

	.ell-text {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		color: $uni-text-color;
		flex: 1;
		margin-top: 26rpx;
		font-weight: bold;
		margin-left: 26rpx;
		font-size: $uni-font-size-lg;
	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-ssm;
	}

	.margin {
		margin: 30rpx 30rpx 30rpx 30rpx;
	}
</style>
