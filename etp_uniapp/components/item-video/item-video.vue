<!-- 已迭代为新版本 此版本不再使用 -->
<template>
	<view class="page">
		<view class="content">

			<video id="myVideo" :src="coursewareInfo.kjdz" :controls="true" style="width: 100%;"
				:enable-progress-gesture="true" @ended="end()" @play="play()" :poster="teachCover(coursewareInfo.fm)" ></video>


			<view class="btn-full" @click="fullScreen()" hover-class="u-hover-class" v-if="false">
				<image src="../../static/lesson/icon-fullscreen.png" class="img-full" mode="widthFix"></image>
				<text>全屏观看</text>
			</view>
			<view
				style="background: rgba(227, 227, 227, 0.2); border-radius: 10rpx; padding: 30rpx;margin: 30rpx 30rpx 0rpx 30rpx;">
				<text
					style="color: #FFFFFF; font-size: 38rpx; margin-bottom: 20rpx;font-weight: bold;">{{coursewareInfo.mc}}</text>
				<u--text :lines="line" :text="coursewareInfo.ms" color="#ffffff" mode="text" size="14">
				</u--text>
			</view>

			<view style="text-align: right; margin-right: 30rpx; " @click="expandClick()"
				v-if="coursewareInfo.ms.length>110">
				<text class="text-all">{{expandTips}}</text>
			</view>
			<button class="btn-next" @click="nextClick()"
				hover-class="u-hover-class">{{isFinal?"本课程到此结束":"下一节"}}</button>


		</view>
	</view>
</template>

<script>
	export default {


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
			},

		},

		mounted() {
			this.videoContext = uni.createVideoContext('myVideo', this);
		},
		data() {
			return {
				line: 4,
				expandTips: "查看完整简介",

			};
		},
		methods: {
			//视频开始继续播放时触发
			play(){
				this.$emit("study",this.coursewareInfo.id);//通知父组件更新进度
			},
			
			//视频播放结束
			end() {
				uni.showToast({
					title: '视频播放结束，正在为您自动跳转下一节',
					icon: 'none'
				});

				setTimeout(()=> {
					this.$emit("next", "");
				}, 1000);

			},
			fullScreen() {
				this.videoContext.requestFullScreen();
			},

			expandClick() {
				if (this.line == 4) {
					this.expandTips = "收起"
					this.line = 100;
				} else {
					this.expandTips = "查看完整简介"
					this.line = 4;
				}

			},
			nextClick() {
				if (this.isFinal) {
					uni.showToast({
						title: '本课程到此结束',
						icon: 'none'
					});
				} else {
					this.$emit("next", "video");
				}
				
			}


		}

	}
</script>

<style lang="scss">
	page {
		background-color: #000000;
		position: relative;
	}

	.content {
		background-color: #000000;
		height: auto;
		padding-bottom: 600rpx;
	}

	.video {
		width: 100%;
	}

	.btn-full {
		width: 180rpx;
		height: 68rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		background: rgba(227, 227, 227, 0.2);
		border: 1px solid #E3E3E3;
		border-radius: 50px;
		color: #FFFFFF;
		font-size: $uni-font-size-sm;
		margin: 66rpx auto;
	}


	.btn-next {
		background-color: #FFFFFF;
		boder: 2px solid #FFFDFF;
		color: $uni-text-color;
		border-radius: 10px;
		font-size: $uni-font-size-base;
max-width: 300rpx;

		right: 0;
		margin-top: 80rpx;
		margin-right: 30rpx;
	}

	.text-info {
		color: #FFFFFF;
		font-size: $uni-font-size-base;
	}

	.img-full {
		width: 30rpx;
		height: 30rpx;
		margin-right: 12rpx;
	}

	.text-all {

		background-color: rgba(227, 227, 227, 0.2);
		;
		border-radius: 10rpx;
		color: #FFFFFF;
		padding: 6rpx 12rpx;
		margin-bottom: 30rpx;
		font-size: 24rpx;
	}
</style>
