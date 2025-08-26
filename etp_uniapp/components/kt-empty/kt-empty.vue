<!-- 空白页组件 -->
<template>
	<view class="flex-center">

		<image class="image-normal" :src="emptyImage" mode="widthFix"></image>
		<text class="text-tips">{{tips}}</text>
	</view>
</template>

<script>
	/**
	 * 空白页组件 
	 * @description 空白页组件，当数据为空时显示 可自定义文字和图片
	 * @property {String} tips 提示词
	 * @property {String} emptyImage 显示图片路径地址  可以设置默认图片
	 * @example <kt-empty tips="暂无数据" ></kt-empty>
	*/
	export default {
		name: "kt-empty",
		props: {
			tips: {
				type: String,
				default: "暂无数据"
			},
			emptyImage: {
				type: String,
				default: "../../static/exam/icon-exam-undone.png"
			},
		},
		
		data() {
			return {
			};
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
		margin-top: 50rpx;
		color: #909299;
		font-size: 28rpx;
	}
</style>
