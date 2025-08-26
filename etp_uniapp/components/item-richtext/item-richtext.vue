<!-- 已迭代为新版本 此版本不再使用 -->
<template>
	<view class="page">
		<text class="title">{{coursewareInfo.mc}}</text>
		<text class="sub">{{coursewareInfo.createBy}} {{coursewareInfo.createTime}}</text>
		<!-- 内容 -->
		<view style="margin-bottom:50rpx ;">
			<mp-html :content="coursewareInfo.nr"></mp-html>
		</view>
		<view  :class="[isFinal?'btn-end':'btn-primary']" @click="nextClick()" hover-class="u-hover-class">{{isFinal?"本课程到此结束":"下一节"}}</view>
	</view>
</template>

<script>
	export default {
		name: "item-richtext",
		props: {
			//课件详情
			coursewareInfo: {
				type: Object,
				default: function() {
					return {}
				}
			},
			//是否最后一节
			isFinal: {
				type: Boolean,
				default: function() {
					return false
				}
			}
		},
		mounted() {
		},
		data() {
			return {
				//点击下一节
				nextClick() {
					if (this.isFinal) {
						uni.showToast({
							title: '本课程到此结束',
							icon: 'none'
						});
					} else {
						this.$emit("next", "");
					}

				}
			};
		}
	}
</script>

<style lang="scss">
	.page {
		position: relative;
		background-color: #FFFFFF;
		height: 100%;
		display: flex;
		flex-direction: column;
		padding: 30rpx;
		margin-top: 24rpx;
		margin-left: 30rpx;
		border-radius: 10rpx;
		margin-right: 30rpx;
		margin-bottom: 30rpx;
	}

	.btn-next {
		background-color: #FFFFFF;
		boder: 2px solid #FFFDFF;
		color: $uni-text-color;
		border-radius: 30px;
		font-size: $uni-font-size-base;
		width: 200rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
		right: 0;
		margin-top: 80rpx;
		margin-right: 30rpx;
	}

	.btn-primary {
		border-radius: 10rpx;
		width: 400rpx;
		background-color: $uni-color-primary;
		color: #FFFFFF;
		padding: 20rpx;
		margin: 0 auto;
		margin-bottom: 60rpx;
		font-size: $uni-font-size-base;
		text-align: center;
	}
	.btn-end {
		border-radius: 10rpx;
		width: 400rpx;
		background-color: $uni-bg-color-grey;
		color: #666666;
		padding: 20rpx;
		margin: 0 auto;
		margin-bottom: 60rpx;
		font-size: $uni-font-size-base;
		text-align: center;
	}

	.title {
		font-size: $uni-font-size-xl;
		color: $uni-text-color;
		font-weight: bold;
	}

	.sub {
		font-size: $uni-font-size-sm;
		color: $uni-text-color-grey;
		margin-top: 24rpx;
		margin-bottom: 24rpx;
	}
</style>
