<!--问答悬浮按钮组件-->
<template>
	<movable-area class="moveA">
		<movable-view class="moveV" :x="x" :y="y" direction="all">
			<view class="float-btn" @click="btnClick">
				<uni-icons type="chatboxes" color="#007aff" size="30"></uni-icons>
			</view>
		</movable-view>
	</movable-area>
	
</template>

<script>
	
	export default {
		props: {			
			
		},
		data() {
			return {
				x:300,
				y:uni.getSystemInfoSync().windowHeight - 60
			}
		},
		
		methods: {
			/**
			 * 点击去到问答页面
			 */
			btnClick() {
				uni.navigateTo({
					url: '/pages/deepAsk/deepAsk',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
		},
	}
</script>

<style scoped lang="scss">
	.moveA {
	  //  可移动的范围  具体值根据需求自行调节
	  height: 100vh;
	  width: 100vw;
	  right: 0rpx;
	  bottom: 100rpx;
	  position: fixed;
	  pointer-events: none;
	  z-index: 99999999;
	}
	.moveV {
	  width: 100rpx;
	  height: 100rpx;
	  pointer-events: auto;
	}
	.float-btn {
		// position: fixed;
		// right: 10px;
		// bottom: 60px;
		width: 100rpx;
		height: 100rpx;
		line-height: 100rpx;
		border-radius: 50%;
		box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.4);
		background-color: rgba(245, 245, 245, 0.8);
		text-align: center;
		// z-index: 999;
	}
</style>
