<template>
	<view>

		<z-paging ref="paging" v-model="list" @query="queryNoticeList" class="z-paging">

			<view v-for="(item,index) in list" :key="index" @click="onClick(item)" class="card">

				<text class="text-bold">{{item.noticeTitle}}</text>
				<text :class="[item.noticeType==1?'bg-type-tz':'bg-type-gg']">{{item.noticeType==1?"通知":"公告"}}</text>
				<text class="text-small-grey ">{{item.createBy}} {{item.createTime}}</text>


			</view>
		</z-paging>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],

			};
		},
		methods: {

			queryNoticeList(pageNo, pageSize) {
				let self = this;
				uni.showLoading({
					title: '加载中',
					mask: false
				});

				let params = {
					"pageNum": pageNo,
					"pageSize": pageSize,
					"mc": this.keyword,
				}
				uni.$u.api.getNoticeList(params).then(res => {
					uni.hideLoading();
					self.$refs.paging.completeByTotal(res.rows,res.total,true);

				});
			},
			onClick(item){
				uni.navigateTo({
					url: '/pages/notice/notice?noticeId='+item.noticeId,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F8F8F8;
	}

	.card {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		margin: 30rpx;
		padding: 30rpx;
		border-radius: 20rpx;
		background-color: #FFFFFF;
	}



	.text-bold {
		color: $uni-color-title;
		font-size: $uni-font-size-title;
		font-weight: bold;
	}

	.text-small-grey {
		margin-top: 24rpx;
		color: $uni-color-subtitle;
		font-size: $uni-font-size-subtitle;

	}

	.bg-type-tz {
		background-color: rgba(47, 204, 167, 0.1);
		color: #2FCCA7;
		font-size: $uni-font-size-sm;
		padding: 3rpx 10rpx;
		border-radius: 10rpx;
		border: solid 2rpx rgba(47, 204, 167, 0.2);
		margin-top: 20rpx;
	}
	.bg-type-gg {
		background-color: rgba(254, 168, 1, 0.1);
		color: #FEA801;
		font-size: $uni-font-size-sm;
		padding: 3rpx 10rpx;
		border-radius: 10rpx;
		border: solid 2rpx rgba(254, 168, 1, 0.2);
		margin-top: 20rpx;
	}
</style>
