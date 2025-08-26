<!-- 错误页面-->
<template>
	<view class="flex-center">

		<image class="image-normal" :src="errorImage" mode="widthFix"></image>
		<view class="text-tips">{{errMsg}}</view>
		<button class="btn-home" @click="back()">返回</button>
	</view>
</template>

<script>
	export default {
		onLoad(options) {
			this.title = options.title;
			this.errMsg = options.errMsg;
			uni.setNavigationBarTitle({
				title: this.title
			})

		},
		data() {
			return {
				title: '错误提示',
				errMsg: '',
				errorImage: '../../../static/common/error.png'
			}
		},
		methods: {

			//返回
			back() {
				uni.navigateBack({
					delta: 1
				});
				// uni.switchTab({
				// 	url: '/pages/index/index'
				// })
			}
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

	.image-normal {
		width: 500rpx;
		margin-top: 300rpx;
	}

	.text-tips {
		margin: 50rpx;
		color: #606266;
		font-size: 32rpx;
		text-align: center;
		word-break: break-word;
	}

	.btn-home {
		width: 50%;
		background: linear-gradient(to right, #49aef1, #037EFF);
		border-radius: 50rpx;
		color: #ffffff;
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.35);
	}
</style>
