<!-- 已迭代为新版本 此版本不再使用 -->
<template>
	<view class="root">
		<view class="silk-ribbon4">链接</view>
		<view class="content">
			<!-- <image src="../../static/study/icon-link.png" class="image"></image> -->
			<image :src="teachCover(coursewareInfo.fm)" class="image-cover" mode="widthFix"></image>
			<text class="title">{{coursewareInfo.mc}}</text>
			<text class="sub">{{coursewareInfo.ms}}</text>
			<text class="tip">本节课为外部链接网页 请点击下方链接开始学习</text>

			<!-- #ifdef MP-WEIXIN -->
			<text class="tip">若打开链接失败提示非业务域名 请复制链接自行从手机浏览器中打开</text>
			<!-- #endif -->
			<view class="text-link" @click="openUrl()">{{coursewareInfo.lj}}</view>


			<view class="btn-primary" hover-class="u-hover-class" @click="copyLink()">复制链接</view>
			<view :class="[isFinal?'btn-grey':'btn-third']" hover-class="u-hover-class" @click="nextClick()">
				{{isFinal?"本课程到此结束":"下一节"}}
			</view>



		</view>

	</view>
</template>

<script>
	export default {
		name: "item-webview",
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
				disabled: false, //按钮禁用状态  默认不禁用
			};
		},
		methods: {

			openUrl() {
				this.$emit("study", this.coursewareInfo.id); //通知父组件更新进度
				this.disabled = false;
				this.$emit("openUrl", "");


			},
			//点击下一节
			nextClick() {
				if (this.isFinal) {
					uni.showToast({
						title: '本课程到此结束',
						icon: 'none'
					});
				} else {
					this.$emit("next", "网页外链");
				}

			},
			//复制链接
			copyLink() {
				this.$emit("study", this.coursewareInfo.id); //通知父组件更新进度
				uni.setClipboardData({
					data: this.coursewareInfo.lj,
					success() {
						uni.showToast({
							title: '复制链接成功'
						});

					}
				})
			}
		}

	}
</script>

<style lang="scss">
	.root {
		position: relative;
		float: center;
		background-color: #FFFFFF;
		// height: 100%;
		width: 90%;
		margin: 0 auto;
		margin-top: 24rpx;
		border-radius: 20rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
	}


	.image {
		width: 128rpx;
		height: 128rpx;
		margin-top: 100rpx;
	}

	.title {
		font-size: 32rpx;
		color: #303133;
		font-weight: bold;
		margin-top: 60rpx;
	}

	.sub {
		font-size: 28rpx;
		color: #666666;
		margin: 30rpx 30rpx 0rpx 30rpx;
	}

	.tip {
		font-size: 24rpx;
		color: #999999;
		margin: 20rpx 30rpx;
	}

	.text-link {
		color: #037EFF;
		width: 80%;
		font-size: 40rpx;
		font-style: italic;
		text-decoration: underline;
		margin-top: 70rpx;
		// margin-bottom: 100rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		word-wrap: break-word; //自动换行
	}

	.btn-primary {
		border-radius: 10rpx;
		width: 400rpx;
		background-color: $uni-color-primary;
		color: white;
		margin-top: 100rpx;
		margin-bottom: 30rpx;
		padding: 20rpx;
		font-size: $uni-font-size-base;
		text-align: center;
	}

	.btn-third {
		border-radius: 10rpx;

		width: 400rpx;
		background-color: rgba(0, 125, 255, 0.1);
		margin: 15rpx;
		margin-bottom: 60rpx;
		padding: 20rpx;
		color: $uni-color-primary;
		// border: 1rpx solid rgba(0, 125, 255, 0.2);
		text-align: center;
		font-size: $uni-font-size-base;
	}

	.btn-grey {
		border-radius: 10rpx;

		width: 400rpx;
		background-color: $uni-bg-color-grey;
		margin: 15rpx;
		margin-bottom: 60rpx;
		padding: 20rpx;
		color: #666666;
		// border: 1rpx solid rgba(0, 125, 255, 0.2);
		text-align: center;
		font-size: $uni-font-size-base;
	}

	.silk-ribbon4 {
		position: absolute;
		top: 15px;
		color: white;
		font-size: $uni-font-size-sm;
		padding: 6rpx 10rpx;
		background: $uni-color-primary;
		border-top-right-radius: 10rpx;
		border-bottom-right-radius: 10rpx;
		box-shadow: -1px 2px 4px rgba(0, 0, 0, 0.5);
		z-index: 999;
	}

	.silk-ribbon4:before,
	.silk-ribbon4:after {
		position: absolute;
		content: "";
		display: block;
	}

	.silk-ribbon4:before {
		width: 7px;
		height: 100%;
		padding: 0 0 7px;
		top: 0;
		left: -7px;
		background: inherit;
		border-radius: 5px 0 0 5px;
	}

	.silk-ribbon4:after {
		width: 5px;
		height: 5px;
		background: rgba(0, 0, 0, 0.35);
		bottom: -5px;
		left: -5px;
		border-radius: 5px 0 0 5px;
	}

	.image-cover {
		width: 100%;
		height: 320rpx;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
	}
</style>
