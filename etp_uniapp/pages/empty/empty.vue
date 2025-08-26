<!-- 为方便演示做的空白页面 -->
<template>
	<view class="flex-center">

		<image class="image-normal" :src="emptyImage" mode="widthFix"></image>
		<text class="text-tips">{{tips}}</text>
	</view>
</template>

<script>
	export default {
		onLoad(options) {
			this.title = options.title;
			uni.setNavigationBarTitle({
				title: this.title
			})
			if (this.title == '直播预约') {
				this.tips = "当前暂无可以预约的直播"
				// this.emptyImage= "../../static/exam/icon-down.png";
			} else {
				this.tips = "当前暂未获得证书";
				// this.emptyImage= "../../static/exam/icon-exam-undone.png";
			}
		},
		data() {
			return {
				title: '',
				tips: '',
				emptyImage:'../../static/exam/icon-exam-undone.png'
			}
		},
		methods: {

		}
	}
</script>

<style>
	.flex-center {
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100vh;
		width: 100vw;
		
		
	}
	.image-normal{
		width: 500rpx;
		margin-top: 300rpx;
	}
	.text-tips{
		margin-top: 50rpx;
		color: #909299;
		font-size: 28rpx;
	}
</style>
