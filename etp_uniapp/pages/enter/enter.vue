<template>
	<view class="page">

		<image :src="teachCover(enter.hdfm)" class="image-cover" mode="widthFix"></image>
		<view class="white-box">
			<view class=" flex-row-between">
				<text class="text-title">{{enter.hdmc}}</text>
				<!-- <text class="text-subtitle">已有{{enter.num}}人报名</text> -->
				<text class="text-subtitle">已有{{enter.num}}人报名</text>

			</view>
			<text class="text-subtitle">{{enter.hdfmc?enter.hdfmc:''}}</text>

 
		</view>
		<view class="flex-row">
			<text class="text-time-title">时间</text>
			<count-down tipText="距报名开始" :end-time="enter.kssj" background="rgba(254, 168, 1, 0.1)" color="#FEA801"
				text-color="#919199" v-if="enter.status==-1" />
			<count-down tipText="距报名结束" :end-time="enter.jssj" background="rgba(254, 168, 1, 0.1)" color="#FEA801"
				text-color="#919199" v-if="enter.status==0" />

			<text class="text-subtitle" v-if="enter.status==1">{{enter.kssj}}至{{enter.jssj}}</text>


		</view>
		<view class="rich-text">
			<mp-html :content="enter.hdnr"></mp-html>
		</view>
		<view class="footer">
			<view :class="[blueOrGreyBg(enter.status,enter.joinStatus)?'btn-normal':'btn-grey']"
				hover-class="u-hover-class" @click="clickEnterBtn(enter.status,enter.joinStatus) ">
				{{btnText(enter.status,enter.joinStatus)}}
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				isEntered: false,
				enterId: '',
				enter: {},

			};
		},
		onLoad(option) {

			this.enterId = Number(option.enterId);
			this.getInfo();

		},
		methods: {

			getInfo() {
				uni.showLoading({
					title: '加载中...',
					mask: false
				});
				uni.$u.api.getEnterInfo(this.enterId).then(res => {
					uni.hideLoading()
					this.enter = res.data;
				}).catch(err => {
					uni.hideLoading()
					console.log(err.errMsg);
				})
			},
			//设置按钮状态
			blueOrGreyBg(status, joinStatus) { //活动状态 -1 未开始 0 进行中 1已结束
				//已经结束  灰色背景 展示报名状态 点击提示已经结束
				//正在进行 未报名：蓝色背景 展示立即报名  已经报名：灰色背景 展示取消报名 
				//未开始  展示灰色背景 未开始 点击提示尚未开始
				if (status == -1 || status == 1 || (status == 0 && joinStatus == '已报名')) {
					return false; //灰色背景
				} else {
					return true; //蓝色背景
				}
			},
			btnText(status, joinStatus) {
				if (status == -1) {
					return "报名未开始";
				} else if (status == 0) {
					if (joinStatus == '未报名') {
						return "立即报名";
					} else {
						return "取消报名";
					}

				} else {
					return "报名已截止"
				}
			},




			onChange(e) {
				this.timeData = e
			},

			clickEnterBtn(status, joinStatus) {
				if (status == -1) { //活动未开始
					uni.showToast({
						icon: 'none',
						title: '报名尚未开始'
					});
				} else if (status == 0) { //活动开始了
					if (joinStatus == '未报名') {
						this.clickEnter(false);
					} else {
						this.clickEnter(true);
					}

				} else { //活动结束
					uni.showToast({
						icon: 'none',
						title: '报名已截止'
					});
				}
			},

			//报名或者取消报名  
			clickEnter(isEntered) {
				let joinStatus = 0;
				let content = "";
				if (isEntered) { //已经报名 点击按钮则取消报名
					content = "确定要取消本次活动报名吗";
					joinStatus = 0; //1-报名，0-取消报名
				} else {
					content = "确定要报名此次活动吗";
					joinStatus = 1; //1-报名，0-取消报名
				}
				uni.showModal({
					title: '温馨提示',
					content: content,
					showCancel: true,

					success: res => {
						if (res.confirm) {
							//确认
							this.enterOrCancle(joinStatus);
						}
					},
					fail: () => {},
					complete: () => {}
				});


			},
			//0-报名，1-取消报名
			enterOrCancle(joinStatus) {
				uni.showLoading({
					title: '请稍候...',
					mask: false
				});
				let params = {
					id: this.enterId,
					sfbm: joinStatus,
					kfcd:this.enter.kfcd
				}

				uni.$u.api.enter(params).then(res => {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					});
					this.getInfo(); //重新请求

				}).catch(err => {
					uni.hideLoading()
					console.log(err.errMsg);
				})
			}





		}

	}
</script>

<style lang="scss">
	.page {

		background-color: #f8f8f8;
		height: 100vh;
	}

	.white-box {
		display: flex;
		padding: 30rpx;
		flex-direction: column;
		justify-content: flex-start;
		align-items: flex-start;
		background-color: #FFFFFF;
		margin-top: 20rpx;
	}

	.flex-row {
		display: flex;
		flex-direction: row;
		justify-content: flex-start;
		align-items: center;
		background-color: #FFFFFF;
		margin-top: 20rpx;
		padding: 30rpx;
	}

	.flex-row-between {
		display: flex;
		flex-direction: row;
		align-items: center;
		width: 100%;
		justify-content: space-between;
		margin-bottom: 24rpx;
	}

	.image-cover {
		width: 100%;
	}

	.rich-text {
		margin-top: 24rpx;
		margin-bottom: 150rpx;
		padding: 20rpx 30rpx;
		background-color: #FFFFFF;
	}

	.text-title {
		color: #303133;
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-subtitle {
		color: #909193;
		font-size: 24rpx;
		flex-shrink: 0;

	}

	.text-time-title {
		color: #303133;
		font-size: 20rpx;
		margin-right: 20rpx;
	}

	.footer {
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
		box-shadow: 0 -1rpx 6rpx rgba(0, 0, 0, 0.1);
	}

	.btn-normal {
		background-color: $uni-color-primary;
		font-size: $uni-font-size-base;
		color: #FFFFFF;
		padding: 24rpx 0rpx;
		border-radius: 40rpx;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		width: 80%;
		text-align: center;
	}

	.btn-grey {
		background-color: #EEEEEE;
		font-size: $uni-font-size-base;
		color: $uni-text-color-grey;
		padding: 24rpx 0rpx;
		border-radius: 40rpx;
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		width: 80%;
		text-align: center;
	}

	.time {
		@include flex;
		align-items: center;

		&__custom {
			width: 48rpx;
			padding: 6rpx 12rpx;
			background-color: rgba(254, 168, 1, 0.1);
			border-radius: 4rpx;
			/* #ifndef APP-NVUE */
			display: flex;
			/* #endif */
			justify-content: center;
			align-items: center;

			&__item {
				color: #FEA801;
				font-size: 20rpx;
				text-align: center;
			}
		}

		&__doc {
			color: #919199;
			padding: 0px 4px;
			font-size: 20rpx;
		}

		&__item {
			color: #606266;
			font-size: 15px;
			margin-right: 4px;
		}
	}
</style>
