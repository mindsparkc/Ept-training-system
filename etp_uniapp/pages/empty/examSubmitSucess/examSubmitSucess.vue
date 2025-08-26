<template>
	<view class=" k-flex-col k-flex-col-center k-margin-xl">
		<image src="../../../static/exam/icon-down.png" class="k-img-lg" mode="aspectFit"></image>
		<text class="k-text-xxl k-text-black k-text-bold k-margin-top-l">交卷成功</text>

		<text class="k-text-l k-text-light-grey k-text-center k-margin-top-l" v-if="showScore==0">本次考试不显示成绩</text>


		<text class="k-text-l k-text-light-grey k-text-center k-margin-top-l"
			v-if="showScore==2">成绩将在判卷完成之后显示\n届时可从历史考试中查看</text>

		<text class="k-text-l k-text-light-grey k-text-center k-margin-top-l"
			v-if="showScore==3">成绩将于{{$u.timeFormat(showScoreTime, 'yyyy-mm-dd hh:MM:ss')}}后公布\n届时可从历史考试中查看</text>

		<text class="k-text-l k-text-light-grey k-text-center k-margin-top-l" v-if="showScore==4">本次考试成绩将由人工发布</text>
		<button class="btn-primary" hover-class="u-hover-class" @click="colse()">我知道了</button>
		<button class="btn-normal" hover-class="u-hover-class" @click="catHistory()"  v-if="showScore==1">查看试卷</button>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				showScore: "", //成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
				showScoreTime: "",
				recordId: "",
			}
		},
		onLoad(option) {
			this.showScore = option.showScore
			this.showScoreTime = option.showScoreTime
			this.recordId = option.recordId;
		},
		methods: {
			colse() {
				uni.navigateBack({
					delta: 1
				});
			},
			catHistory() {
				//关闭当前页面  跳转查看考试页面
				uni.redirectTo({
					url: '/pages/exam/catExam/catExam?recordId=' + this.recordId,
				});
			}

		}
	}
</script>

<style>
	/* 	.btn-primary {
		width: 50%;
		background: linear-gradient(to right, #49aef1, #037EFF);
		border-radius: 50rpx;
		color: #ffffff;
		margin-top: 50px;
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.35);
	} */



	.btn-primary {
		width: 50%;
		border-radius: 10rpx;
		/* background-color: #037EFF; */
		background: linear-gradient(to right, #49aef1, #037EFF);
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.35);
		color: white;
		margin-top: 30px;
		font-size: 16px;
	}

	.btn-normal {
		width: 50%;
		border-radius: 10rpx;
		background-color: rgba(0, 125, 255, 0.1);
		color: #037EFF;
		margin-top: 20px;
		margin-bottom: 12px;
		font-size: 16px;
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.1);

	}
</style>
