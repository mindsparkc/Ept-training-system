<template>
	<view class="page">
		<z-paging ref="paging" v-model="list" @query="queryList">
			<!-- 需要固定在顶部不滚动的view放在slot="top"的view中 -->
			<view slot="top">
				<view style="padding: 20rpx 20rpx 0rpx;">
					<u-search placeholder="请输入习题名称" v-model="keyword" bgColor="#ffffff" @search="search"
						:show-action="true" actionText="搜索" :animation="true" @custom="search">
					</u-search>
				</view>
			</view>
			<!-- 习题列表 -->
			<view v-for="(item,index) in list" :key="index" hover-class="u-hover-class" class="bg-exercises"
				@click="itemClick(item)">
				<view class="silk-ribbon2">练习题</view>

				<view>
					<view class="text-ex-title">{{item.name}}
					</view>
					<view style="margin-top: 24rpx;"><text class="text-grey-content">{{item.content?item.content:""}}</text>

					<!-- 	<image style="width: 25rpx; height: 25rpx; vertical-align: middle;"
							src="../../static/exam/icon_exercises.png" mode="widthFix"></image>
						<text class="text-primary-content">共{{item.stgs?item.stgs:0}}题</text> -->
					</view>
				</view>

				<view class="btn-blue">去做题</view>


			</view>
		</z-paging>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				keyword: '',
				list: []
			}
		},
		onShow() {
			if (this.$refs.paging) {
				this.$refs.paging.refresh();
			}
		},
		methods: {
			queryList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"mc": this.keyword,
					"fb": -1
				}
				uni.$u.api.getExamList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				}).catch((err) => {
					uni.hideLoading();
					console.log(err);
				});

			},
			search() {
				this.$refs.paging.reload();
			},
			itemClick(item) {
				uni.navigateTo({
					url: '/pages/exam/examIndex/examIndex?examId=' + item.examId + "&paperId=" + item.sjid +
						"&examTitle=" + item.mc + "&time=" + item.djsc,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
	}

	.tag-box {
		position: absolute;
		top: 24rpx;
		z-index: 99;
	}

	.ell-text {
		color: $uni-text-color;
		font-size: $uni-font-size-base;
		font-weight: bold;
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

	.text-des {
		color: $uni-text-color;
		font-size: $uni-font-size-sm;
		text-overflow: ellipsis;
		word-break: break-all;
		display: -webkit-box;
		/** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical;
		/** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 1;
		/** 显示的行数 **/
		overflow: hidden;
		/** 隐藏超出的内容 **/

	}

	.text-small-grey {
		color: $uni-text-color-grey;
		font-size: $uni-font-size-sm;
		margin-top: 20rpx;

		.text-end-time {
			color: #FEA801;
			margin: 0 10rpx;
		}
	}

	.list-box {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin: 30rpx;
		position: relative;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		flex: 1;
		margin: 24rpx 20rpx 24rpx 0rpx;
		overflow: hidden;
		width: 0;
	}

	.btn-margin {
		margin-top: 14rpx;
		width: 176rpx;
		align-self: flex-end;
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
		font-weight: bold;
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
</style>
