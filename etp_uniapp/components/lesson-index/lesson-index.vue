<template>
	<view>

		<u--image width="100%" height="auto" :src="teachCover(lesson.fmt)" mode="widthFix"></u--image>

		<view class="white-box-top" v-if="lesson.ms&&lesson.ms!=''&&lesson.ms!='null'">
			<view style="color: #303133; font-weight: bold;font-size: 32rpx;">{{lesson.mc}}</view>
			<view style=" line-height: 1.5em; font-size: 28rpx; color:#666666">{{lesson.ms}}</view>
			<text style="color: #909193;font-size: 24rpx; margin-bottom: 30rpx;margin-top: 30rpx;"
				v-if="lesson.kcjd">已学习 {{Math.round(JSON.parse(lesson.kcjd).rate)}}%</text>

			<u-line-progress :percentage="JSON.parse(lesson.kcjd).rate" activeColor="#007DFF" :showText="false"
				height="10" v-if="lesson.kcjd"></u-line-progress>


		</view>


		<view class="white-box-bottom">

			<text class="text-title">目录</text>
			<u-line margin="24rpx 0rpx"></u-line>

			<view style="margin-top: 24rpx;">
				<item-lesson-index :list="lesson.tCurriculumContents" @indexClick="onClick(arguments)"
					:itemClickId='itemClickId' :currentStudyId="currentStudyId"></item-lesson-index>

			</view>

		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				// currentStudyId: -1, //学习进度里面保存的最近正在学习的课件id
				rate: 0, //进度

			}
		},
		props: {
			lesson: {
				type: Object,
				default: function() {
					return {}
				}
			},
			itemClickId: {
				type: Number,
				default: function() {
					return -1
				}
			},

			currentStudyId: {
				type: Number,
				default: function() {
					return -1
				}
			},
		},

		watch: {
			currentStudyId: 'updateCurrentStudyId'
		},
		mounted() {

		},



		methods: {
			updateCurrentStudyId() {
				console.log("updateCurrentStudyId", this.currentStudyId);
			},

			//点击目录
			onClick(val) {
				this.$emit("indexClick", val[1])
			}
		}
	}
</script>

<style lang="scss">
	page {
		// position: relative;
		background-color: #F8F8F8;
		// height: 100%;
		padding-bottom: 200rpx;
	}

	.white-box-top {
		background-color: #FFFFFF;
		border-radius: 10rpx;
		padding: 30rpx;
		margin: 20rpx 30rpx 20rpx 30rpx;
	}

	.white-box-bottom {
		background-color: #FFFFFF;
		border-radius: 10rpx;
		padding: 16rpx 32rpx 16rpx 32rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-bottom: 20rpx;
		margin-top: 30rpx;
	}

	.text-done {
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;

	}

	.text-doing {
		font-size: $uni-font-size-base;
		color: $uni-color-primary;
	}

	.text-undo {
		font-size: $uni-font-size-base;
		color: $uni-text-color;
	}

	.text-small {
		font-size: $uni-font-size-sm;
	}

	.row-center {
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-top: 30rpx;
	}

	.row-top {
		display: flex;
		flex-direction: row;
		align-items: flex-start;
		margin-top: 20rpx;

	}

	.tag-blue {
		background-color: rgba(0, 125, 255, 0.2);

		padding: 6rpx 10rpx 6rpx 10rpx;
		color: $uni-color-primary;
		font-size: $uni-font-size-ssm;
		border-radius: 10rpx;
	}

	.small-image {
		width: 16rpx;
		height: 16rpx;
	}

	.bg-grey {
		border-radius: 50%;
		background-color: #E5E5E5;
		width: 40rpx;
		height: 40rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0
	}

	.bg-blue {
		border-radius: 50%;
		background-color: #EBF5FF;
		width: 40rpx;
		height: 40rpx;
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.line-col {
		width: 2rpx;
		background-color: #e5e5e5;
		height: 100%;

	}

	.text-title {
		font-size: $uni-font-size-lg;
		color: $uni-text-color;
		font-weight: bold;
	}

	.flex-col-center {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.flex-col-start {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		flex: 1;
		margin: 0rpx 30rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		min-height: 80rpx;
		justify-content: space-between;
	}
</style>
