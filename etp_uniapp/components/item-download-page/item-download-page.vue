<template>
	<view class="root">
		<image src="../../static/study/icon-file.png" class="image"></image>
		<text class="title">Vue3 从入门到实战 进阶式掌握</text>
		<text class="sub">文件大小：20MB</text>
		<view style="margin-top: 400rpx">
			<u-button shape="circle" text="下载文件" :loading="true" loadingText="正在下载..." type="primary"></u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style>
	.root {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;

	}

	.image {
		width: 116rpx;
		height: 100rpx;
		margin-top: 164rpx;
	}

	.title {
		font-size: 32rpx;
		color: #303133;
		font-weight: bold;
		margin-top: 50rpx;
	}

	.sub {
		font-size: 28rpx;
		color: #303133;
		margin-top: 50rpx;
	}
</style>
