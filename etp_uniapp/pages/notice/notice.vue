<template>
	<view class="flex-col">
		<view>
			<text class="text-title">{{notice.noticeTitle}}</text>
			<view>
			<text :class="[notice.noticeType==1?'bg-type-tz':'bg-type-gg']">{{notice.noticeType==1?"通知":"公告"}}</text></view>
		</view>
		<text class="text-subtitle">{{notice.createBy}} {{notice.createTime}}</text>
		<!-- 内容 -->
		<mp-html :content="notice.noticeContent"></mp-html>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				noticeId: 0,
				notice: {}
			}
		},
		onLoad(option) {
			this.noticeId = Number(option.noticeId);
			this.getNoticeInfo()
		},
		methods: {
			getNoticeInfo() {
				uni.$u.api.getNoticeInfo(this.noticeId).then(res => {
					this.notice = res.data;
				}).catch(err => {
					console.log(err.errMsg);
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #FFFFFF;
	}

	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 30rpx;
	}



	.text-title {
		color: $uni-color-title;
		font-size: $uni-font-size-title;
		font-weight: bold;
	}

	.text-subtitle {
		margin-top: 24rpx;
		color: $uni-color-subtitle;
		font-size: $uni-font-size-subtitle;
		margin-bottom: 30rpx;

	}

	.bg-type-tz {
		background-color: rgba(47, 204, 167, 0.1);
		color: #2FCCA7;
		font-size: $uni-font-size-sm;
		padding: 3rpx 10rpx;
		border-radius: 10rpx;
		border: solid 2rpx rgba(47, 204, 167, 0.2);
		margin-top: 20rpx;
	}

	.bg-type-gg {
		background-color: rgba(254, 168, 1, 0.1);
		color: #FEA801;
		font-size: $uni-font-size-sm;
		padding: 3rpx 10rpx;
		border-radius: 10rpx;
		border: solid 2rpx rgba(254, 168, 1, 0.2);
		margin-top: 20rpx;
	}
</style>
