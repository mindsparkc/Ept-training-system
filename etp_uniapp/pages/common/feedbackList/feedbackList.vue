<template>
	<view class="page">

		<z-paging ref="paging" v-model="feedbacks" @query="queryfeedback" class="z-paging">
			<view v-for="(item,index) in feedbacks" :key="index" @click="feedbackClick(item)" >
				<view class="list-box" hover-class="hover">
					<view class="text-title">{{item.yjms}}</view>
					<view v-if="item.hfStatus=='0'" class="text-tips">您的反馈我们已经收到，我们会尽快处理并给您回复。感谢您的支持。</view>
					<view v-if="item.hfStatus=='1'" class="text-tips">尊敬的用户您好，关于此反馈我们已进行答复。请注意查看。</view>
					<view class="text-time">{{item.createTime}}</view>
					<view :class="item.hfStatus=='0'?'state-grey':'state-green'  ">{{item.hfStatusName}}</view>
				</view>
			</view>
		</z-paging>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				keyword: '',
				feedbacks: [],
			}
		},
		methods: {
			search() {
				this.$refs.paging.reload();
			},

			//请求
			queryfeedback(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"mc": this.keyword,
					// "tags": this.checkTags
				}
				uni.$u.api.getFeedbackList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				});

				// let self = this;
				// setTimeout(function() {
				// 	uni.hideLoading()
				// 	let feedbacks = [{
				// 			"title": "Vue3从入门到实战进阶式掌握完整知识体系进阶式掌握完整知识体系进阶式掌握完整知识体系",
				// 			"progress": "14",
				// 			"state": 0,
				// 			"startDate": "",
				// 			"endDate": "",
				// 			"tags": [
				// 				"UI",
				// 				"PS"
				// 			],
				// 			"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 		},
				// 		{
				// 			"title": "Vue3从入门",
				// 			"progress": "12",
				// 			"startDate": "",
				// 			"endDate": "",
				// 			"state": 1,
				// 			"tags": ["UI", "PS", "UI", "PS", "UI", "PS", "UI", "PS"],
				// 			"cover": "https://cdn.uviewui.com/uview/swiper/swiper3.png"
				// 		}
				// 	];
				// 	self.$refs.paging.complete(feedbacks);

				// }, 100);
			},
			feedbackClick(item) {

				uni.navigateTo({
					url: '/pages/common/feedbackDetail/feedbackDetail?' + 'feedbackId=' + item.id ,
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

	.text-title {
		color: #303133;
		font-size: 30rpx;
		font-weight: bold;
		margin-right: 100rpx;
	}

	.text-time {
		color: #909193;
		font-size: 24rpx;
		margin-top: 24rpx;
	}

	.text-tips {
		color: #bcbcbc;
		font-size: 24rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		margin-top: 22rpx;
	}

	.state-green {
		position: absolute;
		right: 0;
		top: 0;
		color: #07C160;
		font-size: 28rpx;
		padding: 30rpx;
	}

	.state-grey {
		position: absolute;
		right: 0;
		padding: 30rpx;
		top: 0;
		color: #606266;
		font-size: 28rpx;
	}



	.list-box {
		position: relative;
		background-color: #FFFFFF;
		border-radius: 20rpx;
		margin-top: 30rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		padding: 30rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}
.hover {
		background-color: #eee;
		opacity: 0.6;
	}


	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
	}
</style>
